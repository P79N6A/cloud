package com.springframework.doc;

import com.springframework.butler.configure.EnableSwaggerButler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author xieshengrong swagger 聚合文档服务
 */
@EnableDiscoveryClient
@EnableSwaggerButler
@SpringBootApplication
@Slf4j
public class EurekaSwaggerDocApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaSwaggerDocApplication.class);
        log.warn("swagger 聚合文档服务 started success");
    }
}
