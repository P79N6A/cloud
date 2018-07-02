package com.springframework.gateway.domian.routeconfig.service.impl;

import com.springframework.gateway.domian.routeconfig.entity.RouteConfig;
import com.springframework.gateway.domian.routeconfig.service.RouteConfigService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author summer
 * 2018/7/2
 */
@Service
public class RouteConfigServiceImpl implements RouteConfigService {
    @Override
    public Mono<RouteConfig> save(RouteConfig routeConfig) {
        return null;
    }

    @Override
    public Flux<RouteConfig> findAll() {

        return null;
    }
}
