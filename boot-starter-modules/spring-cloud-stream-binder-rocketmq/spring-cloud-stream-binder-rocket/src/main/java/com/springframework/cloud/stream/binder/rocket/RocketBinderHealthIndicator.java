package com.springframework.cloud.stream.binder.rocket;

import com.springframework.cloud.stream.binder.rocketmq.properties.RocketMQConsumerProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @author summer
 */
public class RocketBinderHealthIndicator  implements HealthIndicator {
    private static final int DEFAULT_TIMEOUT = 60;
    private int timeout = DEFAULT_TIMEOUT;
    private MQPushConsumer metadataConsumer;
    private final RocketMQMessageChannelBinder binder;

    private final Log logger;

    public RocketBinderHealthIndicator(RocketMQMessageChannelBinder binder) {
        this.binder = binder;
        this.logger = LogFactory.getLog(this.getClass());
    }
    @Override
    public Health health() {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        Future<Health> future = exec.submit(this::buildHealthStatus);
        try {
            return future.get(this.timeout, TimeUnit.SECONDS);
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return Health.down()
                    .withDetail("Interrupted while waiting for partition information in", this.timeout + " seconds")
                    .build();
        }
        catch (ExecutionException ex) {
            return Health.down(ex).build();
        }
        catch (TimeoutException ex) {
            return Health.down()
                    .withDetail("Failed to retrieve partition information in", this.timeout + " seconds")
                    .build();
        }
        catch (Exception ex){
            if (this.logger.isWarnEnabled()) {
                this.logger.warn( "Health check failed", ex);
            }
            return Health.down()
                    .withDetail("Health check failed", this.timeout + " seconds")
                    .build();
        }
        finally {
            exec.shutdownNow();
        }
    }

    /**
     * Set the timeout in seconds to retrieve health information.
     *
     * @param timeout the timeout - default 60.
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    private Health buildHealthStatus() {
        try {
            if (this.metadataConsumer == null) {
                synchronized (RocketBinderHealthIndicator.this) {
                    if (this.metadataConsumer == null) {
                        this.metadataConsumer  = new DefaultMQPushConsumer();
                    }
                }
            }
            synchronized (this.metadataConsumer) {
                Set<String> downMessages = new HashSet<>();
                final Map<String, RocketMQMessageChannelBinder.TopicInformation> topicsInUse =
                        RocketBinderHealthIndicator.this.binder.getTopicsInUse();
                if (topicsInUse.isEmpty()) {
                    return Health.down()
                            .withDetail("No topic information available", "Kafka broker is not reachable")
                            .build();
                }
                else {
                    for (String topic : topicsInUse.keySet()) {
                        RocketMQMessageChannelBinder.TopicInformation topicInformation = topicsInUse.get(topic);
//                        if (!topicInformation.isTopicPattern()) {
//                            List<PartitionInfo> partitionInfos = this.metadataConsumer.accept(topic);
//                            for (PartitionInfo partitionInfo : partitionInfos) {
//                                if (topicInformation.getPartitionInfos()
//                                        .contains(partitionInfo) && partitionInfo.leader().id() == -1) {
//                                    downMessages.add(partitionInfo.toString());
//                                }
//                            }
//                        }
                    }
                }
                if (downMessages.isEmpty()) {
                    return Health.up().build();
                }
                else {
                    return Health.down()
                            .withDetail("Following partitions in use have no leaders: ", downMessages.toString())
                            .build();
                }
            }
        }
        catch (Exception ex) {
            return Health.down(ex).build();
        }
    }
}
