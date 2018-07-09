package com.springframework.gateway.config;

import com.springframework.gateway.domain.routeconfig.service.RouteConfigService;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author summer
 * 2018/7/3
 */
@Configuration
public class DynamicRouteConfigure {
    private RedisTemplate redisTemplate;
    private DiscoveryClient discoveryClient;
    private DiscoveryLocatorProperties properties;
    private RouteConfigService routeConfigService;

    public DynamicRouteConfigure(RedisTemplate redisTemplate, DiscoveryClient discoveryClient, DiscoveryLocatorProperties properties, RouteConfigService routeConfigService) {
        this.redisTemplate = redisTemplate;
        this.properties = properties;
        this.discoveryClient = discoveryClient;
        this.routeConfigService = routeConfigService;
    }

    @Bean
    public DiscoveryClientRouteDefinitionLocator dynamicRouteLocator(DiscoveryClient discoveryClient, DiscoveryLocatorProperties properties, RouteConfigService routeConfigService) {
        return new DiscoveryClientRouteDefinitionLocator(discoveryClient,properties,routeConfigService);
    }
//
//    @Bean
//    public MySQLRouteDefinitionRepository mySQLRouteDefinitionRepository(RedisTemplate redisTemplate, RouteConfigService routeConfigService) {
//        return new MySQLRouteDefinitionRepository(redisTemplate, routeConfigService);
//    }

//    @Bean
//    @ConditionalOnProperty(name = "spring.cloud.gateway.discovery.locator.enabled" ,matchIfMissing = true)
//    public DiscoveryClientRouteDefinitionLocator discoveryClientRouteDefinitionLocator(RouteConfigService routeConfigService,DiscoveryClient discoveryClient, DiscoveryLocatorProperties enable) {
//        return new DiscoveryClientRouteDefinitionLocator(routeConfigService,discoveryClient, enable);
//    }
}
