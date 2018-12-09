package org.apache.rocketmq.spring.starter.core;
/**
 * @author summer
 */
public interface RocketMQListener<T> {
    void onMessage(T message);
}