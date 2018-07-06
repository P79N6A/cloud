package com.springframework.gateway.config;

import com.springframework.gateway.domain.routeconfig.service.RouteConfigService;
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
    private RouteConfigService routeConfigService;

    public DynamicRouteConfigure(RedisTemplate redisTemplate, GatewayProperties properties, DiscoveryClient discoveryClient, DiscoveryLocatorProperties enable, RouteConfigService routeConfigService) {
        this.redisTemplate = redisTemplate;
        this.properties = properties;
        this.discoveryClient = discoveryClient;
        this.enable = enable;
        this.routeConfigService = routeConfigService;
    }

    @Bean
    public DynamicRouteDefinitionLocator dynamicRouteLocator(RedisTemplate redisTemplate) {
        return new DynamicRouteDefinitionLocator(redisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(RouteDefinitionRepository.class)
    public MySQLRouteDefinitionRepository mySQLRouteDefinitionRepository(RedisTemplate redisTemplate, RouteConfigService routeConfigService) {
        return new MySQLRouteDefinitionRepository(redisTemplate, routeConfigService);
    }

    @Bean
    public RouteDefinitionLocator discoveryClientRouteDefinitionLocator(RouteConfigService routeConfigService,DiscoveryClient discoveryClient, DiscoveryLocatorProperties enable) {
        return new DiscoveryClientRouteDefinitionLocator(routeConfigService,discoveryClient, enable);
    }
}
