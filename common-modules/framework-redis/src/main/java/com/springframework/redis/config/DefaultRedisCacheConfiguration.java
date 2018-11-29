package com.springframework.redis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author summer
 * 2018/11/21
 */
@Configuration
public class DefaultRedisCacheConfiguration {
    private RedisTemplate  redisTemplate;
    public DefaultRedisCacheConfiguration(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }
//    @Bean
//    GenericCacheManager defaultRedisCacheService(RedisTemplate  redisTemplate) {
//        return new DefaultRedisCacheService(redisTemplate);
//    }
}
