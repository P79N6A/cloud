package com.springframework.http.configure.async;

import com.springframework.http.utils.FakeX509TrustManager;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author summer
 * 2018/8/8
 */
@Configuration
public class MySchemeIOSessionStrategy {

    /** // 设置协议http和https对应的处理socket链接工厂的对象
     * @return
     */
    @Bean
    public Registry<SchemeIOSessionStrategy> sessionStrategyRegistry (){
        SSLContext context;
        try {
            context = SSLContext.getInstance("TLS");
            TrustManager[] _trustManagers = new TrustManager[]{new FakeX509TrustManager()};
            context.init(null, _trustManagers, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new IllegalStateException(e.getMessage());
        }
          Registry<SchemeIOSessionStrategy> sessionStrategyRegistry = RegistryBuilder
                .<SchemeIOSessionStrategy> create()
                .register("http", NoopIOSessionStrategy.INSTANCE)
                .register("https", new SSLIOSessionStrategy(context))
                .build();
          return  sessionStrategyRegistry;
    }



}
