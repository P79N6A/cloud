package com.springframework.http.configure.async;

import org.apache.http.client.config.RequestConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author summer
 * 2018/8/8
 */
@Configuration
public class MyAsyncRequestConfig {

    @Value("${async.httpclient.config.connectTimeout}")
    private int connectTimeout = 2000;

    @Value("${async.httpclient.config.connectRequestTimeout}")
    private int connectRequestTimeout = 2000;

    @Value("${async.httpclient.config.socketTimeout}")
    private int socketTimeout = 2000;




    @Bean
    public RequestConfig asyncConfig() {
        return RequestConfig.custom()
                .setConnectionRequestTimeout(this.connectRequestTimeout)
                .setConnectTimeout(this.connectTimeout)
                .setSocketTimeout(this.socketTimeout)
                .build();
    }
}
