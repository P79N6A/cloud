/**
 *
 */
package com.springframework.common.datasource;

import com.springframework.common.datasource.configure.CatMybatisPlugin;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.List;


/**
 * @author summer
 *
 */
@Configuration
@ConditionalOnBean(DataSource.class)
public class MyBatisPlusAutoConfiguration {
    private static final Log logger = LogFactory.getLog(MyBatisPlusAutoConfiguration.class);


    private List<Interceptor> interceptors;


    public MyBatisPlusAutoConfiguration() {
    }

    @Bean
    @ConditionalOnMissingBean(CatMybatisPlugin.class)
    public CatMybatisPlugin catMybatisPlugin() {
        CatMybatisPlugin catMybatisPlugin=  new CatMybatisPlugin();
        interceptors.add(catMybatisPlugin);
        return catMybatisPlugin;
    }

}
