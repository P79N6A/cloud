package com.springframework.gateway.config;

import com.google.common.collect.Lists;
import com.springframework.gateway.constant.CommonConstant;
import com.springframework.gateway.domain.routeconfig.service.RouteConfigService;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.data.redis.core.RedisTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.synchronizedMap;

/**
 * @author summer 基于mysql的动态路由
 * 2018/7/4
 */
public class MySQLRouteDefinitionRepository implements RouteDefinitionRepository {
    private RedisTemplate redis;
    private final Map<String, RouteDefinition> routes = synchronizedMap(new LinkedHashMap<String, RouteDefinition>());
    private RouteConfigService routeConfigService;

    public MySQLRouteDefinitionRepository(RedisTemplate redis, RouteConfigService routeConfigService) {
        this.redis = redis;
        this.routeConfigService = routeConfigService;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.flatMap(r -> {
            routes.put(r.getId(), r);
            //TODO  save DB

            return Mono.empty();
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap(id -> {
            if (routes.containsKey(id)) {
                routes.remove(id);
                return Mono.empty();
            }
            return Mono.defer(() -> Mono.error(new NotFoundException("RouteDefinition not found: " + routeId)));
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public Flux<RouteDefinition> getRouteDefinitions() {
        return Flux.fromIterable(loadFromDb());
    }

    private List<RouteDefinition> loadFromDb() {
        //TODO load DB 数据
        return Lists.newArrayList();
    }
}
