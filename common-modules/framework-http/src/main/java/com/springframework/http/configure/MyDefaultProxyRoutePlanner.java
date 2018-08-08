package com.springframework.http.configure;

import org.apache.http.HttpHost;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author summer
 * 2018/8/8
 * 描述：HttpClient代理
 */
@Configuration
public class MyDefaultProxyRoutePlanner {
    /**
     * 代理的host地址
     */
    @Value("${httpclient.config.proxyhost}")
    private String proxyHost;

    /**
     * 代理的端口号
     */
    @Value("${httpclient.config.proxyPort}")
    private int proxyPort = 8080;

    @Bean
    public DefaultProxyRoutePlanner defaultProxyRoutePlanner() {
        HttpHost proxy = new HttpHost(this.proxyHost, this.proxyPort);
        return new DefaultProxyRoutePlanner(proxy);
    }
}

