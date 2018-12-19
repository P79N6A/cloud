package com.springframework.feign.configure;

import com.springframework.feign.filter.OriginFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author summer
 * 2018/7/31
 */
@Configuration
@ConditionalOnProperty(value = "security.origin-filter.enabled", matchIfMissing = true)
public class FeignConfiguration extends FeignAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(OriginFilter.class)
    public OriginFilter originFilter() {
        return new OriginFilter();
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegiste = new FilterRegistrationBean();
        filterRegiste.setOrder(9999);
        filterRegiste.setAsyncSupported(true);
        filterRegiste.setFilter(originFilter());
        return filterRegiste;
    }
}
