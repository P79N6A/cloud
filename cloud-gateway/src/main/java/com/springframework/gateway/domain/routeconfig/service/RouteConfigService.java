package com.springframework.gateway.domain.routeconfig.service;

import com.springframework.gateway.domain.routeconfig.entity.RouteConfig;
import org.springframework.cloud.gateway.route.RouteDefinition;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author summer
 * 2018/7/2
 */
public interface RouteConfigService {
    /**
     * 保存路由配置
     *
     * @param routeConfig
     * @return
     */
    Mono<Integer> save(RouteConfig routeConfig);

    /**
     * 查询所有路由配置
     *
     * @return
     */
    Flux<List<RouteDefinition>> findAll();


    /**查询路由
     * @param serviceId 服务id
     * @return
     */
    Mono<RouteConfig> findRouteConfig(String serviceId);
}
