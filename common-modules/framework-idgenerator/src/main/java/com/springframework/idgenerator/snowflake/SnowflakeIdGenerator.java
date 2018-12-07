package com.springframework.idgenerator.snowflake;

import com.springframework.idgenerator.DistributedIdGenerator;
import com.springframework.redis.GenericCacheRedisManager;

/**
 * @author summer
 * 2018/8/20
 */
public class SnowflakeIdGenerator implements DistributedIdGenerator {

    /**
     * 使用Redis保存workerId
     */
    private GenericCacheRedisManager redisCacheService;
    private int idType;
    public SnowflakeIdGenerator(GenericCacheRedisManager redisCacheService, int idType) {
        this.redisCacheService = redisCacheService;
        this.idType = idType;
    }
}
