package com.springframework.feign.configure;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.Principal;

/**
 * @author summer
 * 2018/8/1
 */

@Configuration
public class FeignConfig implements RequestInterceptor {
    @Value("${spring.application.name}")
    private String clientServiceId;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("X-FeignOrigin", clientServiceId);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Principal principal = attributes.getRequest().getUserPrincipal();

    }

}

