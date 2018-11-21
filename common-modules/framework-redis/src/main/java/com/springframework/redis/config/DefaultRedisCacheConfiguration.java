package com.springframework.redis.config;

import com.springframework.cache.GenericCacheManager;
import com.springframework.redis.DefaultRedisCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author summer
 * 2018/11/21
 */
@Configuration
public class DefaultRedisCacheConfiguration {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Bean
    GenericCacheManager defaultRedisCacheService() {
        DefaultRedisCacheService cacheService = new DefaultRedisCacheService(redisTemplate);
        return cacheService;
    }
}
