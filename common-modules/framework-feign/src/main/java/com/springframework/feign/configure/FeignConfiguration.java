package com.springframework.feign.configure;

import com.springframework.feign.filter.OriginFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author summer
 * 2018/7/31
 */
@ConfigurationProperties(prefix = "security.origin-filter.enabled")
public class FeignConfiguration extends FeignAutoConfiguration {

    @Bean
    public OriginFilter originFilter() {
        return new OriginFilter();
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(originFilter());
        return filterRegistrationBean;
    }
}
