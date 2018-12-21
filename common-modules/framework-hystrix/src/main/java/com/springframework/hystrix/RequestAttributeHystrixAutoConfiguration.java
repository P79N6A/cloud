package com.springframework.hystrix;

import com.netflix.hystrix.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author summer
 * 2018/12/19
 */
@Slf4j
@Configuration
@ConditionalOnClass(HystrixCommand.class)
@ConditionalOnProperty(value = "hystrix.propagate.request-attribute.enabled", matchIfMissing = true)
public class RequestAttributeHystrixAutoConfiguration {

    @Bean
    RequestAttributeHystrixConcurrencyStrategy requestAttributeHystrixConcurrencyStrategy() {
        log.info("自定义 Request Attribute Hystrix ConcurrencyStrategy  initialized  ");
        return new RequestAttributeHystrixConcurrencyStrategy();
    }
}
