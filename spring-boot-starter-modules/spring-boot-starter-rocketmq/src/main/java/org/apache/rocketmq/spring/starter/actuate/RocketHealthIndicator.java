package org.apache.rocketmq.spring.starter.actuate;

import org.apache.rocketmq.spring.starter.core.RocketMQTemplate;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.util.Assert;

/**
 * @author summer
 */
public class RocketHealthIndicator extends AbstractHealthIndicator {

    private final RocketMQTemplate rocketMQTemplate;

    public RocketHealthIndicator(RocketMQTemplate rocketMQTemplate) {
        super("Rocket health check failed");
        Assert.notNull(rocketMQTemplate, "RocketMQTemplate must not be null");
        this.rocketMQTemplate = rocketMQTemplate;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        builder.up().withDetail("namesrvAddr", this.getNamesrvAddr())
                .withDetail("clientIP", this.getClientIP())
                .withDetail("instanceName", this.getInstanceName())
                .withDetail("defaultTopicQueueNums", this.getDefaultTopicQueueNums())
                .withDetail("heartbeatBrokerInterval", this.getHeartbeatBrokerInterval())
                .withDetail("producerGroup", this.getProducerGroup());
    }

    private int getHeartbeatBrokerInterval() {
        return this.rocketMQTemplate.getProducer().getHeartbeatBrokerInterval();
    }

    private String getNamesrvAddr() {
        return this.rocketMQTemplate.getProducer().getNamesrvAddr();
    }

    private String getClientIP() {
        return this.rocketMQTemplate.getProducer().getClientIP();
    }

    private int getDefaultTopicQueueNums() {
        return this.rocketMQTemplate.getProducer().getDefaultTopicQueueNums();
    }

    private String getInstanceName() {
        return this.rocketMQTemplate.getProducer().getInstanceName();
    }

    private String getProducerGroup() {
        return this.rocketMQTemplate.getProducer().getProducerGroup();
    }
}
