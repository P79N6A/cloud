package com.springframework.redis.config;

import com.springframework.redis.DuplicateProtectCacheManager;
import com.springframework.redis.GenericCacheRedisManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author summer
 * 2018/11/21
 */
@Configuration
public class RedisCacheConfiguration {
    private RedisTemplate redisTemplate;

    public RedisCacheConfiguration(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Bean
    GenericCacheRedisManager duplicateProtectCacheManager(RedisTemplate redisTemplate) {
        return new DuplicateProtectCacheManager(redisTemplate);
    }
}
