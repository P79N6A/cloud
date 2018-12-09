package org.apache.rocketmq.spring.starter.enums;
/**
 * @author summer
 */
public enum ConsumeMode {
    /**
     * receive asynchronously delivered messages concurrently
     */
    CONCURRENTLY,

    /**
     * receive asynchronously delivered messages orderly. one queue, one thread
     */
    ORDERLY
}