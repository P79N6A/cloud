package com.springframework.http.configure;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolException;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author summer
 * 2018/8/8
 */
@Configuration
public class MyDefaultRedirectStrategy {
    private static final transient Logger DB_LOGGER = LoggerFactory.getLogger(MyDefaultRedirectStrategy.class);

    @Bean("defaultRedirectStrategy")
    public DefaultRedirectStrategy defaultRedirectStrategy() {
        return new DefaultRedirectStrategy() {
            @Override
            public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) {
                boolean isRedirect = false;
                try {
                    isRedirect = super.isRedirected(request, response, context);
                } catch (ProtocolException e) {
                    DB_LOGGER.error("isRedirected:" + e);
                }
                if (!isRedirect) {
                    int responseCode = response.getStatusLine().getStatusCode();
                    if (responseCode == /*301*/HttpStatus.SC_MOVED_PERMANENTLY || responseCode == /*302*/HttpStatus.SC_MOVED_TEMPORARILY) {
                        return true;
                    }
                }
                return isRedirect;
            }

        };
    }

}
