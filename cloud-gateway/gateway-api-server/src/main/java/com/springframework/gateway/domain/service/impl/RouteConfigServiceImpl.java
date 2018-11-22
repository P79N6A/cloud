package com.springframework.gateway.domain.service.impl;

import com.springframework.gateway.domain.dto.RouteConfigDTO;
import com.springframework.gateway.domain.entity.RouteConfig;
import com.springframework.gateway.domain.repository.impl.RouteConfigDaoImpl;
import com.springframework.gateway.domain.service.RouteConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author summer
 * 2018/7/2
 */
@Service("routeConfigService")
public class RouteConfigServiceImpl implements RouteConfigService {
    private RedisTemplate redisTemplate;
    private RouteConfigDaoImpl routeConfigDao;

    @Autowired
    public RouteConfigServiceImpl(RedisTemplate redisTemplate, RouteConfigDaoImpl routeConfigDao) {
        this.redisTemplate = redisTemplate;
        this.routeConfigDao = routeConfigDao;
    }


    @Override
    public boolean saveRouteConfig(RouteConfig routeConfig) {
        return routeConfigDao.saveRouteConfig(routeConfig);
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
