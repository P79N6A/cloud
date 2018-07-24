package com.springframework.common.datasource;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.springframework.common.datasource.masterslave.SpringBootMasterSlaveRuleConfigurationProperties;
import com.springframework.common.datasource.sharding.DataSourceUtil;
import com.springframework.common.datasource.sharding.ShardingProperties;
import com.springframework.common.datasource.sharding.SpringBootShardingRuleConfigurationProperties;
import io.shardingjdbc.core.api.MasterSlaveDataSourceFactory;
import io.shardingjdbc.core.api.ShardingDataSourceFactory;
import io.shardingjdbc.core.constant.ShardingPropertiesConstant;
import io.shardingjdbc.core.exception.ShardingJdbcException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

/**
 * @author summer
 */
@Configuration
@EnableConfigurationProperties({SpringBootShardingRuleConfigurationProperties.class, SpringBootMasterSlaveRuleConfigurationProperties.class})
public class ShardingAutoConfiguration implements EnvironmentAware {

    private SpringBootShardingRuleConfigurationProperties shardingProperties;
    private SpringBootMasterSlaveRuleConfigurationProperties masterSlaveProperties;
    private final Map<String, DataSource> dataSourceMap = Maps.newHashMap();
    private final Properties props = new Properties();

    @Autowired
    public ShardingAutoConfiguration(SpringBootShardingRuleConfigurationProperties shardingProperties, SpringBootMasterSlaveRuleConfigurationProperties masterSlaveProperties) {
        this.shardingProperties = shardingProperties;
        this.masterSlaveProperties = masterSlaveProperties;
    }

    @Bean
    public DataSource dataSource() throws SQLException {
        return null == masterSlaveProperties.getMasterDataSourceName() ? ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingProperties.getShardingRuleConfiguration(), props)
                : MasterSlaveDataSourceFactory.createDataSource(dataSourceMap, masterSlaveProperties.getMasterSlaveRuleConfiguration());
    }

    @Override
    public void setEnvironment(final Environment environment) {
        setDataSourceMap(environment);
        setShardingProperties(environment);
    }

    private void setDataSourceMap(final Environment environment) {
        String dataSources = environment.getProperty(ShardingProperties.JDBC_DATASOURCE_NAME_PREFIX);
        if (StringUtils.isEmpty(dataSources)) {
            throw new ShardingJdbcException("Can't find dataSources names properties!");
        }
        for (String name : dataSources.split(",")) {
            try {
                Map<String,Object> dataSourcePropsMap =
                        Binder.get(environment).
                                bind(ShardingProperties.DATASOURCE_PREFIX + name, Bindable.of(Map.class)).orElseThrow(
                                () -> new FatalBeanException("Could not bind DataSourceSettings properties"));
                Preconditions.checkState(dataSourcePropsMap != null, "Wrong datasource properties!");
                DataSource dataSource = DataSourceUtil.getDataSource(dataSourcePropsMap.get("type").toString(), dataSourcePropsMap);
                dataSourceMap.put(name, dataSource);
            } catch (final ReflectiveOperationException ex) {
                throw new ShardingJdbcException("Can't find datasource type!", ex);
            }
        }
    }

    private void setShardingProperties(final Environment environment) {
        Map<String,Object> configProperties = null;
        try {
            configProperties = Binder.get(environment).
                    bind(ShardingProperties.SHARDING_PREFIX, Bindable.of(Map.class)).orElseThrow(
                    () -> new FatalBeanException("Could not bind ShardingProperties properties")
            );
        } catch (FatalBeanException e) {
            e.printStackTrace();
        }
        String showSQL = "";
        String executorSize = "";
        if (!CollectionUtils.isEmpty(configProperties)) {
            showSQL= configProperties.get("sql-show").toString();
            executorSize = configProperties.get("executor-size").toString();
        }
        if (!Strings.isNullOrEmpty(showSQL)) {
            props.setProperty(ShardingPropertiesConstant.SQL_SHOW.getKey(), showSQL);
        }
        if (!Strings.isNullOrEmpty(executorSize)) {
            props.setProperty(ShardingPropertiesConstant.EXECUTOR_SIZE.getKey(), executorSize);
        }
    }
}
