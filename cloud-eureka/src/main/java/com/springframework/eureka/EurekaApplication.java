package com.springframework.eureka;

import com.springframework.swgger.config.SwaggerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Import;

/** @author summer 服务发现注册中心 */
@SpringCloudApplication
@EnableEurekaServer
@EnableDiscoveryClient
@Import(SwaggerConfig.class)
@Slf4j
public class EurekaApplication {

  public static void main(String[] args) {
    SpringApplication.run(EurekaApplication.class, args);
    log.warn("服务发现注册中心 started success ");
  }
}
