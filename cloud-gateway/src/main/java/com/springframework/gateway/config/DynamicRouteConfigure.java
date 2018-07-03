package com.springframework.gateway.config;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author summer
 * 2018/7/3
 */
@Configuration
public class DynamicRouteConfigure {
    private DiscoveryClient discoveryClient;
    private String routeIdPrefix;
    private Registration registration;
    private ServerProperties server;
    private RedisTemplate redisTemplate;
    private GatewayProperties properties;
    private RouteLocatorBuilder builder;

    public DynamicRouteConfigure(RouteLocatorBuilder builder, GatewayProperties properties, DiscoveryClient discoveryClient, String routeIdPrefix, Registration registration, ServerProperties server, RedisTemplate redisTemplate) {
        this.discoveryClient = discoveryClient;
        this.properties = properties;
        this.routeIdPrefix = routeIdPrefix;
        this.registration = registration;
        this.server = server;
        this.redisTemplate = redisTemplate;
    }

    @Bean
    public RouteLocator dynamicRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes().build();
    }
}
