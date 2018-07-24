package com.springframework.common.datasource.masterslave;

import io.shardingjdbc.core.yaml.masterslave.YamMasterSlaveRuleConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Master slave rule configuration properties.
 *
 * @author summer
 */
@ConfigurationProperties(prefix = "sharding.jdbc.config.masterslave")
public class SpringBootMasterSlaveRuleConfigurationProperties extends YamMasterSlaveRuleConfiguration {
}
