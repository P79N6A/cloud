package com.springframework.cloud.stream.binder.rocketmq.properties;

/**
 * @author summer
 */
public class RocketMQBindingProperties {

    private RocketMQConsumerProperties consumer = new RocketMQConsumerProperties();

    private RocketMQProducerProperties producer = new RocketMQProducerProperties();

    public RocketMQConsumerProperties getConsumer() {
        return consumer;
    }

    public void setConsumer(RocketMQConsumerProperties consumer) {
        this.consumer = consumer;
    }

    public RocketMQProducerProperties getProducer() {
        return producer;
    }

    public void setProducer(RocketMQProducerProperties producer) {
        this.producer = producer;
    }
}
