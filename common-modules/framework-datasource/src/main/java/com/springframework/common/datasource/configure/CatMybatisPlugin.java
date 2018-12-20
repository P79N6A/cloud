package com.springframework.common.datasource.configure;

/**
 * @author summer
 * 2018/12/12
 */

import com.alibaba.druid.pool.DruidDataSource;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.transaction.SpringManagedTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.ReflectionUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;

@Intercepts({@Signature(
        method = "query",
        type = Executor.class,
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
), @Signature(
        method = "update",
        type = Executor.class,
        args = {MappedStatement.class, Object.class}
)})
public class CatMybatisPlugin implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(CatMybatisPlugin.class);
    private static final Map<String, String> sqlURLCache = new ConcurrentHashMap<>(256);
    private static final String EMPTY_CONNECTION = "jdbc:mysql://unknown:3306/%s?useUnicode=true";
    private Executor target;

    public CatMybatisPlugin() {
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        String[] strArr = mappedStatement.getId().split("\\.");
        String methodName = strArr[strArr.length - 2] + "." + strArr[strArr.length - 1];
        boolean catIsPresent = false;

        try {
            catIsPresent = Class.forName("com.dianping.cat.Cat") != null;
        } catch (Throwable var21) {
        }

        if (!catIsPresent) {
            return invocation.proceed();
        } else {
            Transaction t = Cat.newTransaction("SQL", methodName);
            Object parameter = null;
            if (invocation.getArgs().length > 1) {
                parameter = invocation.getArgs()[1];
            }

            BoundSql boundSql = mappedStatement.getBoundSql(parameter);
            Configuration configuration = mappedStatement.getConfiguration();
            String sql = this.showSql(configuration, boundSql);
            Cat.logEvent("SQL.Statement", sql.substring(0, sql.indexOf(" ")), Message.SUCCESS, sql);
            SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
            Cat.logEvent("SQL.Method", sqlCommandType.name().toLowerCase(), Message.SUCCESS, sql);
            String s = this.getSQLDatabase();
            Cat.logEvent("SQL.Database", s);
            Object returnObj = null;

            try {
                returnObj = invocation.proceed();
                t.setStatus(Message.SUCCESS);
            } catch (Exception var19) {
                t.setStatus(var19);
                Cat.logError(var19);
            } finally {
                t.complete();
            }

            return returnObj;
        }
    }

    private DataSource getDataSource() {
        org.apache.ibatis.transaction.Transaction transaction = this.target.getTransaction();
        if (transaction == null) {
            logger.error(String.format("Could not find transaction on target [%s]", this.target));
            return null;
        } else if (transaction instanceof SpringManagedTransaction) {
            String fieldName = "dataSource";
            Field field = ReflectionUtils.findField(transaction.getClass(), fieldName, DataSource.class);
            if (field == null) {
                logger.error(String.format("Could not find field [%s] of type [%s] on target [%s]", fieldName, DataSource.class, this.target));
                return null;
            } else {
                ReflectionUtils.makeAccessible(field);
                return (DataSource) ReflectionUtils.getField(field, transaction);
            }
        } else {
            logger.error(String.format("---the transaction is not SpringManagedTransaction:%s", transaction.getClass().toString()));
            return null;
        }
    }

    private String getSqlURL() {
        DataSource dataSource = this.getDataSource();
        if (dataSource == null) {
            return null;
        } else {
            if (dataSource instanceof AbstractRoutingDataSource) {
                String methodName = "determineTargetDataSource";
                Method method = ReflectionUtils.findMethod(AbstractRoutingDataSource.class, methodName);
                if (method == null) {
                    logger.error(String.format("---Could not find method [%s] on target [%s]", methodName, dataSource));
                    return null;
                }

                ReflectionUtils.makeAccessible(method);
                DataSource dataSource1 = (DataSource) ReflectionUtils.invokeMethod(method, dataSource);
                if (dataSource1 instanceof DruidDataSource) {
                    DruidDataSource druidDataSource = (DruidDataSource) dataSource1;
                    return druidDataSource.getUrl();
                }

                logger.error("---only surpport DruidDataSource:" + dataSource1.getClass().toString());
            } else if (dataSource instanceof DruidDataSource) {
                return ((DruidDataSource) dataSource).getUrl();
            }

            return null;
        }
    }

    private String getSQLDatabase() {
        String dbName = "DEFAULT";

        String url = sqlURLCache.get(dbName);
        if (url != null) {
            return url;
        } else {
            url = this.getSqlURL();
            if (url == null) {
                url = String.format("jdbc:mysql://unknown:3306/%s?useUnicode=true", dbName);
            }

            sqlURLCache.put(dbName, url);
            return url;
        }
    }

    public String showSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterMappings.size() > 0 && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(this.getParameterValue(parameterObject)));
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                Iterator var8 = parameterMappings.iterator();

                while (var8.hasNext()) {
                    ParameterMapping parameterMapping = (ParameterMapping) var8.next();
                    String propertyName = parameterMapping.getProperty();
                    Object obj;
                    if (metaObject.hasGetter(propertyName)) {
                        obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(this.getParameterValue(obj)));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(this.getParameterValue(obj)));
                    }
                }
            }
        }

        return sql;
    }

    private String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(2, 2, Locale.CHINA);
            value = "'" + formatter.format((Date) obj) + "'";
        } else if (obj != null) {
            value = obj.toString();
        } else {
            value = "";
        }

        return value;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            this.target = (Executor) target;
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
