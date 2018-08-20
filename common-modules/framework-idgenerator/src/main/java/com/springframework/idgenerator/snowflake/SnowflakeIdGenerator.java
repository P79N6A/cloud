package com.springframework.idgenerator.snowflake;

import com.springframework.idgenerator.DistributedIdGenerator;
import com.springframework.redis.RedisCacheService;

/**
 * @author summer
 * 2018/8/20
 */
public class SnowflakeIdGenerator implements DistributedIdGenerator {

    /**
     * 使用Redis保存workerId
     */
    private RedisCacheService redisCacheService;
    private int idType;
    public SnowflakeIdGenerator(RedisCacheService redisCacheService, int idType) {
        this.redisCacheService = redisCacheService;
        this.idType = idType;
    }
}
