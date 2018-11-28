package com.springframework.redis.config;

import com.springframework.cache.GenericCacheManager;
import com.springframework.redis.DefaultRedisCacheService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author summer
 * 2018/11/21
 */
@Configuration
public class DefaultRedisCacheConfiguration {
    private RedisTemplate<String, Object> redisTemplate;
    public DefaultRedisCacheConfiguration(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }
    @Bean
    GenericCacheManager defaultRedisCacheService() {
        return new DefaultRedisCacheService(redisTemplate);
    }
}
