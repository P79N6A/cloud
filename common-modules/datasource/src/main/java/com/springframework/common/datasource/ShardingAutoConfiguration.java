package com.springframework.common.datasource;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.springframework.common.datasource.masterslave.SpringBootMasterSlaveRuleConfigurationProperties;
import com.springframework.common.datasource.sharding.JdbcConfig;
import com.springframework.common.datasource.sharding.SpringBootShardingRuleConfigurationProperties;
import io.shardingjdbc.core.api.MasterSlaveDataSourceFactory;
import io.shardingjdbc.core.api.ShardingDataSourceFactory;
import io.shardingjdbc.core.constant.ShardingPropertiesConstant;
import io.shardingjdbc.core.exception.ShardingJdbcException;
import io.shardingjdbc.core.util.DataSourceUtil;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
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
        String dataSources = environment.getProperty(JdbcConfig.DatasourceProperties.PREFIX + "names");
        if(StringUtils.isEmpty(dataSources)){throw new ShardingJdbcException("Can't find dataSources names properties!");}
        for (String name : dataSources.split(",")) {
            try {
                JdbcConfig.DatasourceProperties dataSourceProps = Binder.get(environment).bind(JdbcConfig.DatasourceProperties.PREFIX + name, Bindable.of(JdbcConfig.DatasourceProperties.class)).orElseThrow(
                        () -> new FatalBeanException("Could not bind DataSourceSettings properties"));

                Preconditions.checkState(dataSourceProps != null, "Wrong datasource properties!");
                Map<String, Object> dataSourcePropsMap = convertToMap(dataSourceProps);

                DataSource dataSource = DataSourceUtil.getDataSource(dataSourceProps.getType().toString(), dataSourcePropsMap);
                dataSourceMap.put(name, dataSource);
            } catch (final ReflectiveOperationException ex) {
                throw new ShardingJdbcException("Can't find datasource type!", ex);
            }
        }
    }

    private Map<String, Object> convertToMap(JdbcConfig.DatasourceProperties dataSourceProps) {
        Map<String, Object> dataSourcePropsMap = Maps.newHashMap();
        try {
            BeanUtils.copyProperties(dataSourceProps, dataSourcePropsMap);
        } catch (BeansException e) {
            throw new ShardingJdbcException("Can't convert dataSourceProps to dataSourcePropsMap ", e);
        }
        return dataSourcePropsMap;
    }

    private void setShardingProperties(final Environment environment) {
        JdbcConfig.ConfigProperties configProperties = Binder.get(environment).bind(JdbcConfig.ConfigProperties.PREFIX, Bindable.of(JdbcConfig.ConfigProperties.class)).orElseThrow(
                () -> new FatalBeanException("Could not bind DataSourceSettings properties"));
        String showSQL = configProperties.getSqlShow();
        if (!Strings.isNullOrEmpty(showSQL)) {
            props.setProperty(ShardingPropertiesConstant.SQL_SHOW.getKey(), showSQL);
        }
        String executorSize = configProperties.getExecutorSize();
        if (!Strings.isNullOrEmpty(executorSize)) {
            props.setProperty(ShardingPropertiesConstant.EXECUTOR_SIZE.getKey(), executorSize);
        }
    }
}
