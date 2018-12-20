package org.apache.rocketmq.spring.starter.core;
/**
 * @author summer
 */
public interface RocketMQConsumerLifecycleListener<T> {
    void prepareStart(final T consumer);
}