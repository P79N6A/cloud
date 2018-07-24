package com.springframework.common.datasource.sharding;

import io.shardingjdbc.core.yaml.sharding.YamlShardingRuleConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Sharding rule configuration properties.
 *
 * @author caohao
 */
@ConfigurationProperties(prefix = SpringBootShardingRuleConfigurationProperties.SHARDINGRULE_CONFIGURATION_PROPERTIES_PREFIX)
public class SpringBootShardingRuleConfigurationProperties extends YamlShardingRuleConfiguration {
    public static final String SHARDINGRULE_CONFIGURATION_PROPERTIES_PREFIX= "sharding.jdbc.config.sharding";
}
