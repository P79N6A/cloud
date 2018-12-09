package com.springframework.cloud.stream.binder.rocketmq.utils;

import org.apache.rocketmq.client.Validators;
import org.apache.rocketmq.client.exception.MQClientException;


/**
 * @author summer
 */
public final class RocketMQTopicUtils {

    private RocketMQTopicUtils() {

    }

    /**
     * Allowed chars are ASCII alphanumerics, '.', '_' and '-'.
     */
    public static void validateTopicName(String topicName) {
        try {
            Validators.checkTopic(topicName);
        } catch (MQClientException e) {
            throw new AssertionError(e);
        }
    }
}