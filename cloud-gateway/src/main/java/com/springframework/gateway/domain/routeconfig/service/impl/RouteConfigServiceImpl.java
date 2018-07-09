package com.springframework.gateway.domain.routeconfig.service.impl;

import com.google.common.collect.Lists;
import com.springframework.gateway.constant.CommonConstant;
import com.springframework.gateway.domain.routeconfig.dto.RouteConfigDTO;
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
        return routeConfigDao.save(routeConfig);
    }


    @Override
    public List<RouteDefinition> findAll() {
        return routeConfigDao.findAll();
    }

    @Override
    public RouteConfigDTO findRouteConfig(String serviceId) {
        return routeConfigDao.findRouteConfig(serviceId);
    }
}
