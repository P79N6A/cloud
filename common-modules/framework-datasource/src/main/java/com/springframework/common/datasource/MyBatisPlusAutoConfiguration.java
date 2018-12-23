/**
 *
 */
package com.springframework.common.datasource;

import com.springframework.common.datasource.actuator.DataSourceHealthIndicator;
import com.springframework.common.datasource.configure.CatMybatisPlugin;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


/**
 * @author summer
 *
 */
@Slf4j
@Configuration
@ConditionalOnBean(DataSource.class)
public class MyBatisPlusAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean(CatMybatisPlugin.class)
    public CatMybatisPlugin catMybatisPlugin() {
        log.info(" CatMybatisPlugin  initialized ");
        return new CatMybatisPlugin();
    }

    @Bean
    @ConditionalOnBean(Executor.class)
    public DataSourceHealthIndicator dataSourceHealthIndicator(){
        return new DataSourceHealthIndicator();
    }
}
