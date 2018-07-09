package com.springframework.sms;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/** @author summer */
@EnableDiscoveryClient
@Slf4j
@MapperScan("com.springframework.sms.domain.mapper")
@SpringBootApplication
public class CloudSmsApplication {

  public static void main(String[] args) {
    SpringApplication.run(CloudSmsApplication.class, args);
  }
}
