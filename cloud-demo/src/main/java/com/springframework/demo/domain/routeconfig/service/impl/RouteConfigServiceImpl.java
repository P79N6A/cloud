package com.springframework.demo.domain.routeconfig.service.impl;

import com.google.common.collect.Lists;
import com.springframework.demo.domain.routeconfig.entity.RouteConfig;
import com.springframework.demo.domain.routeconfig.repository.RouteConfigDao;
import com.springframework.demo.domain.routeconfig.service.RouteConfigService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private RouteConfigDao routeConfigDao;

    @Autowired
    public RouteConfigServiceImpl(RouteConfigDao routeConfigDao) {
        this.routeConfigDao = routeConfigDao;
    }

    @Override
    public Mono<RouteConfig> save(RouteConfig routeConfig) {
        routeConfigDao.save(routeConfig);
        return null;
    }

    @Override
    public Flux<RouteConfig> findAll() {
        return null;
    }

    @Override
    public List<RouteConfig> findRouteConfigList() {
        RouteConfig routeConfig = routeConfigDao.findRouteConfig("CLOUD-DEMO");
        return Lists.newArrayList(routeConfig);
    }

}
