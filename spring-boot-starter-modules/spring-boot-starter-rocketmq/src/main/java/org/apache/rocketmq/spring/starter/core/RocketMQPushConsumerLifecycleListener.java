package org.apache.rocketmq.spring.starter.core;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
/**
 * @author summer
 */
public interface RocketMQPushConsumerLifecycleListener extends RocketMQConsumerLifecycleListener<DefaultMQPushConsumer> {
}
