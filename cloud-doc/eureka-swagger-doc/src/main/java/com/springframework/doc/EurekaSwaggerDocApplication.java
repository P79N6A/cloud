package com.springframework.doc;

import com.springframework.butler.configure.EnableSwaggerButler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xieshengrong swagger 聚合文档服务
 */
@EnableDiscoveryClient
@EnableSwaggerButler
@SpringBootApplication
@ComponentScan(basePackages = {"com.springframework.doc", "com.springframework.butler.configure"})
@Slf4j
@RestController
@EnableBinding({Source.class, Sink.class})
public class EurekaSwaggerDocApplication {
    @Autowired
    private ProducerRunner producerRunner;

    public static void main(String[] args) {
        SpringApplication.run(EurekaSwaggerDocApplication.class);
        log.warn("swagger 聚合文档服务 started success");
    }

    @RequestMapping("/heath")
    public String heath() throws Exception {
        producerRunner.run("测试发生");
        return "OK";
    }

    @StreamListener("input")
    public void receiveInput1(String receiveMsg) {
        System.out.println("input1 receive: " + receiveMsg);
    }

    @Bean
    Command command() {
        return new Command();
    }

   class Command implements CommandLineRunner{
        @Autowired
       ProducerRunner producerRunner;
       @Override
       public void run(String... args) throws Exception {
           producerRunner.run(args);
       }
   }

}
