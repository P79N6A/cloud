package com.springframework.http.configure.async;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author summer
 * 2018/8/8
 */
@Configuration
public class MyCredentialsProvider {
    @Value("${async.httpclient.username}")
    private String username = "";
    @Value("${async.httpclient.password}")
    private String password = "";

    /**
     * @return 认证信息
     */
    @Bean
    public CredentialsProvider credentialsProvider() {
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
                username, password);
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);
        return credentialsProvider;
    }
}
