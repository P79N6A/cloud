package com.springframework.eureka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/** @author summer 服务发现注册中心 */
@SpringCloudApplication
@EnableEurekaServer
@Slf4j
public class EurekaApplication {

  public static void main(String[] args) {
    SpringApplication.run(EurekaApplication.class, args);
    log.warn("服务发现注册中心 started success ");
  }
}
