package com.springframework.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.cloud.gateway.handler.predicate.RoutePredicateFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionRouteLocator;
import org.springframework.data.redis.core.RedisTemplate;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author summer
 * 2018/7/3
 */
@Slf4j
public class DynamicRouteDefinitionLocator extends RouteDefinitionRouteLocator {
    private GatewayProperties properties;
    private RedisTemplate redisTemplate;

    public DynamicRouteDefinitionLocator(RouteDefinitionLocator routeDefinitionLocator, List<RoutePredicateFactory> predicates, List<GatewayFilterFactory> gatewayFilterFactories, GatewayProperties gatewayProperties) {
        super(routeDefinitionLocator, predicates, gatewayFilterFactories, gatewayProperties);
    }

    @Override
    public Flux<Route> getRoutes() {
        return super.getRoutes();
    }
}
