package com.springframework.cloud.stream.binder.rocket.config;

import com.springframework.cloud.stream.binder.rocket.RocketMQMessageChannelBinder;
import com.springframework.cloud.stream.binder.rocketmq.properties.JaasLoginModuleConfiguration;
import com.springframework.cloud.stream.binder.rocketmq.properties.RocketMQBinderConfigurationProperties;
import com.springframework.cloud.stream.binder.rocketmq.properties.RocketMQExtendedBindingProperties;
import com.springframework.cloud.stream.binder.rocketmq.provisioning.RocketMQTopicProvisioner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.config.codec.kryo.KryoCodecAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.cloud.stream.binder.Binder;
import org.springframework.integration.codec.Codec;

/**
 * @author summer
 */
@Configuration
@ConditionalOnMissingBean(Binder.class)
@Import({ KryoCodecAutoConfiguration.class, PropertyPlaceholderAutoConfiguration.class })
@EnableConfigurationProperties({ RocketMQBinderConfigurationProperties.class, RocketMQExtendedBindingProperties.class })
public class RocketBinderConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(RocketBinderConfiguration.class);

    @Autowired
    private Codec codec;

    @Autowired
    private RocketMQBinderConfigurationProperties configurationProperties;

    @Autowired
    private RocketMQExtendedBindingProperties rocketmqExtendedBindingProperties;

    // @Autowired
    // private ProducerListener producerListener;

    @Autowired
    private ApplicationContext context;

    @Bean
    RocketMQTopicProvisioner provisioningProvider() {
        return new RocketMQTopicProvisioner(this.configurationProperties);
    }

    @Bean
    RocketMQMessageChannelBinder rocketmqMessageChannelBinder() {
        RocketMQMessageChannelBinder messageChannelBinder = new RocketMQMessageChannelBinder(
                this.configurationProperties, provisioningProvider());
        messageChannelBinder.setCodec(this.codec);
//         messageChannelBinder.setProducerListener(producerListener);
        messageChannelBinder.setExtendedBindingProperties(this.rocketmqExtendedBindingProperties);
        return messageChannelBinder;
    }

    public static class JaasConfigurationProperties {

        private JaasLoginModuleConfiguration rocketmq;

    }
}
