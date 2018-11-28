package com.springframework.gateway;

import com.springframework.redis.config.DefaultRedisCacheConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author summer
 * 2018/11/22
 */
@SpringCloudApplication
@Configuration
@Import(DefaultRedisCacheConfiguration.class)
@Slf4j
public class Bootstrap {

    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class, args);
        log.warn("Gateway网关服务 started success");
    }

}
