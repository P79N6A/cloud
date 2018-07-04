package com.springframework.gateway.domain.routeconfig.service.impl;

import com.google.common.collect.Lists;
import com.springframework.gateway.constant.CommonConstant;
import com.springframework.gateway.domain.routeconfig.service.RouteConfigService;
import com.springframework.gateway.domain.routeconfig.entity.RouteConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author summer
 * 2018/7/2
 */
@Service
public class RouteConfigServiceImpl implements RouteConfigService {
    private RedisTemplate redisTemplate;

    @Autowired
    public RouteConfigServiceImpl(@Qualifier("redisTemplate") RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Mono<RouteConfig> save(RouteConfig routeConfig) {
        return null;
    }

    @Override
    public Flux<RouteConfig> findAll() {
        List<RouteDefinition> routeDefinitionList = (List<RouteDefinition>) redisTemplate.opsForHash().entries(CommonConstant.ROUTE_KEY).values();
        List<RouteConfig> result = Lists.newArrayList();

        routeDefinitionList.forEach(e -> {
            RouteConfig routeConfig = new RouteConfig();
            result.add(routeConfig);
        });
        return Flux.fromIterable(result);
    }
}
