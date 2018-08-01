package com.springframework.feign.configure;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author summer
 * 2018/8/1
 */

@Configuration
public class FeignConfig implements RequestInterceptor {
    @Value("${spring.application.name}")
    private String clientServiceId;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("X-FeignOrigin", clientServiceId);
    }

}

