package com.springframework.feign.configure;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author summer
 * 2018/8/1
 */

public class FeignConfig implements RequestInterceptor {
    @Value("${spring.application.name}")
    private String clientServiceId;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String X_FEIGNORIGIN_HEADER = "X-FeignOrigin";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        //添加 X-FeignOrigin
        requestTemplate.header(X_FEIGNORIGIN_HEADER, clientServiceId);
        final HttpServletRequest request = getHttpServletRequest();
        if(Objects.isNull(request)){
            return;
        }
        Map<String, String> headers = getHeaders(request);
        for (String headerName : headers.keySet()) {
            requestTemplate.header(headerName, getHeaders(getHttpServletRequest()).get(headerName));
        }
    }

    private HttpServletRequest getHttpServletRequest() {
        try {
            return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        } catch (Exception e) {
            //ignore
            return null;
        }
    }

    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> map = new LinkedHashMap<>();
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }
}

