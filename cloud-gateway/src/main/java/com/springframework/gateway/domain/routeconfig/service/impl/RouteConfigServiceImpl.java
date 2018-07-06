package com.springframework.gateway.domain.routeconfig.service.impl;

import com.google.common.collect.Lists;
import com.springframework.gateway.constant.CommonConstant;
import com.springframework.gateway.domain.routeconfig.repository.impl.RouteConfigDaoImpl;
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
import java.util.Optional;

/**
 * @author summer
 * 2018/7/2
 */
@Service
public class RouteConfigServiceImpl implements RouteConfigService {
    private RedisTemplate redisTemplate;
    private RouteConfigDaoImpl routeConfigDao;

    @Autowired
    public RouteConfigServiceImpl(RedisTemplate redisTemplate, RouteConfigDaoImpl routeConfigDao) {
        this.redisTemplate = redisTemplate;
        this.routeConfigDao = routeConfigDao;
    }


    @Override
    public Integer save(RouteConfig routeConfig) {
        Integer result = routeConfigDao.save(routeConfig);
        return result;
    }


    @Override
    public List<RouteDefinition> findAll() {
        List<RouteDefinition> routeDefinitionList = routeConfigDao.findAll();
        return routeDefinitionList;
    }

    @Override
    public RouteConfig findRouteConfig(String serviceId) {
        RouteConfig result = routeConfigDao.findRouteConfig(serviceId);
        return result;
    }
}
