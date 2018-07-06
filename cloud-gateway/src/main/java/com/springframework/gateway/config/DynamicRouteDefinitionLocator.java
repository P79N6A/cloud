package com.springframework.gateway.config;

import com.google.common.collect.Lists;
import com.springframework.gateway.constant.CommonConstant;
import com.springframework.gateway.domain.routeconfig.entity.RouteConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.data.redis.core.RedisTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Instant;
import java.util.*;

import static java.util.Collections.synchronizedMap;

/**
 * @author summer  基于redis方式做动态路由
 * 2018/7/3
 */
@Slf4j
public class DynamicRouteDefinitionLocator implements RouteDefinitionRepository {
    private RedisTemplate redis;
    private final Map<String, RouteDefinition> routes = synchronizedMap(new LinkedHashMap<String, RouteDefinition>());
    DynamicRouteDefinitionLocator(RedisTemplate redisTemplate) {
        this.redis = redisTemplate;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Void> save(Mono<RouteDefinition> route) {
        RouteConfig routeConfig = new RouteConfig();
        Date curr =new Date(Instant.now().getEpochSecond());
        StringBuilder filtersSts = new StringBuilder();
        StringBuilder predicateListStr = new StringBuilder();
        List<FilterDefinition> filters =null;
        List<PredicateDefinition> predicateList =null;
        if(route.blockOptional().isPresent()){
            filters=Objects.requireNonNull(route.block()).getFilters();
            filters.forEach( e ->{
                filtersSts.append(e.toString()).append("-");
            });
            predicateList=Objects.requireNonNull(route.block()).getPredicates();
            predicateList.forEach(e ->{
                predicateListStr.append(e.toString()).append("-");
            });
        }
        routeConfig.setRouteId(route.block().getId());
        routeConfig.setCreateTime(curr);
        routeConfig.setUpdateTime(curr);
        routeConfig.setFilters(filtersSts.toString());
        routeConfig.setPredicates(predicateListStr.toString());
        routeConfig.setStatus(true);
        return route.flatMap(r -> {
            redis.opsForHash().put(CommonConstant.ROUTE_KEY, r.getId(), routeConfig);
            return Mono.empty();
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap(id -> {
            if (0 != redis.opsForHash().delete(CommonConstant.ROUTE_KEY, id)) {
                return Mono.empty();
            }
            return Mono.defer(() -> Mono.error(new NotFoundException("RouteDefinition not found: " + routeId)));
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public Flux<RouteDefinition> getRouteDefinitions() {
        List<RouteConfig> routeConfigs = (List<RouteConfig>) redis.opsForHash().entries(CommonConstant.ROUTE_KEY).values();
        List<RouteDefinition> routeDefinitionList = Lists.newArrayList();
        routeConfigs.forEach(e -> {
            RouteDefinition routeDefinition = new RouteDefinition();
            routeDefinitionList.add(routeDefinition);
            routeDefinition.setId(e.getRouteId());
            routeDefinition.setUri(URI.create(e.getUri()));
            routeDefinition.setFilters(e.getFilterList());
            routeDefinition.setOrder(e.getOrder());
            routeDefinition.setPredicates(e.getPredicateList());
        });
        return Flux.fromIterable(routeDefinitionList);
    }
}
