package com.springframework.gateway.domian.routeconfig.service;

import com.springframework.gateway.domian.routeconfig.entity.RouteConfig;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    Mono<RouteConfig> save(RouteConfig routeConfig);

    /**
     * 查询所有路由配置
     *
     * @return
     */
    Flux<RouteConfig> findAll();
}
