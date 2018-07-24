package com.springframework.common.datasource.sharding;

import lombok.Data;

/**
 * @author summer
 * 2018/7/24
 */
@Data
public class ShardingProperties {
    public static final String JDBC_DATASOURCE_NAME_PREFIX = "sharding.jdbc.datasource.names";
    public static final String DATASOURCE_PREFIX = "sharding.jdbc.datasource.";
    public static final String SHARDING_PREFIX = "sharding.jdbc.config.sharding.props";


}
