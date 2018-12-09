package com.springframework.cloud.stream.binder.rocket.config;

import com.springframework.cloud.stream.binder.rocket.RocketBinderHealthIndicator;
import com.springframework.cloud.stream.binder.rocket.RocketMQMessageChannelBinder;
import com.springframework.cloud.stream.binder.rocketmq.properties.RocketMQBinderConfigurationProperties;
import com.springframework.cloud.stream.binder.rocketmq.properties.RocketMQConsumerProperties;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author summer
 */
@Configuration
@ConditionalOnClass(name = "org.springframework.boot.actuate.health.HealthIndicator")
@ConditionalOnEnabledHealthIndicator("binders")
public class RocketBinderHealthIndicatorConfiguration {
    /**
     * @param rocketMessageChannelBinder
     * @param configurationProperties
     * @return
     */
    @Bean
    RocketBinderHealthIndicator rocketBinderHealthIndicator(RocketMQMessageChannelBinder rocketMessageChannelBinder,
                                                            RocketMQBinderConfigurationProperties configurationProperties) {
        RocketBinderHealthIndicator indicator = new RocketBinderHealthIndicator(rocketMessageChannelBinder);
        indicator.setTimeout(configurationProperties.getHealthTimeout());
        return indicator;
    }

}
