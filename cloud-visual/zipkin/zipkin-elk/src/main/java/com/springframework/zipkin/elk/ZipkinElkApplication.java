package com.springframework.zipkin.elk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import zipkin.server.EnableZipkinServer;

/**
 * @author summer
 * 2018/8/10
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableZipkinServer
public class ZipkinElkApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZipkinElkApplication.class, args);
    }
}
