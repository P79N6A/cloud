package com.springframework.http.configure.async;

import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author summer
 * 2018/8/8
 */
@Configuration
public class MyIOReactorConfig {

    /**
     * @return 配置io线程
     */
    @Bean
    public IOReactorConfig ioReactorConfig() {
        return IOReactorConfig.custom()
                .setIoThreadCount(Runtime.getRuntime().availableProcessors())
                .build();
    }
}
