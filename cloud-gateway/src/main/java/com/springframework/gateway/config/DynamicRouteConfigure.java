package com.springframework.gateway.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author summer
 * 2018/7/3
 */
@Configuration
public class DynamicRouteConfigure {
    private RedisTemplate redisTemplate;
    private GatewayProperties properties;
    private DiscoveryClient discoveryClient;
    private DiscoveryLocatorProperties enable;

    public DynamicRouteConfigure(DiscoveryLocatorProperties enable, DiscoveryClient discoveryClient, GatewayProperties properties, RedisTemplate redisTemplate) {
        this.properties = properties;
        this.redisTemplate = redisTemplate;
        this.discoveryClient = discoveryClient;
        this.enable = enable;
    }

    @Bean
    @ConditionalOnMissingBean(RouteDefinitionRepository.class)
    public DynamicRouteDefinitionLocator dynamicRouteLocator(RedisTemplate redisTemplate) {
        return new DynamicRouteDefinitionLocator(redisTemplate);
    }

//    @Bean
//    public MySQLRouteDefinitionRepository mySQLRouteDefinitionRepository(RedisTemplate redisTemplate) {
//        return new MySQLRouteDefinitionRepository(redisTemplate);
//    }
//
//    @Bean
//    public RouteDefinitionLocator discoveryClientRouteDefinitionLocator(DiscoveryClient discoveryClient, DiscoveryLocatorProperties enable) {
//        return new DiscoveryClientRouteDefinitionLocator(discoveryClient, enable);
//    }
}
