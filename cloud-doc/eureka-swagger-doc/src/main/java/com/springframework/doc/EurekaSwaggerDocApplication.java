package com.springframework.doc;

import com.springframework.butler.configure.EnableSwaggerButler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xieshengrong swagger 聚合文档服务
 */
@EnableDiscoveryClient
@EnableSwaggerButler
@SpringBootApplication
@ComponentScan(basePackages = {"com.springframework.doc","com.springframework.butler.configure"})
@Slf4j
@RestController
public class EurekaSwaggerDocApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaSwaggerDocApplication.class);
        log.warn("swagger 聚合文档服务 started success");
    }

    @RequestMapping("/heath")
    public String heath(){
        return "OK";
    }
}
