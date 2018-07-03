package com.springframework.gateway.domian.routeconfig.repository.impl;

import com.springframework.gateway.domian.routeconfig.mapper.RouteConfigMapper;
import com.springframework.gateway.domian.routeconfig.repository.RouteConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author summer
 * 2018/7/2
 */
@Repository
public class RouteConfigRepositoryImpl implements RouteConfigRepository {

    private final RouteConfigMapper routeConfigMapper;

    @Autowired
    public RouteConfigRepositoryImpl(RouteConfigMapper routeConfigMapper) {
        this.routeConfigMapper = routeConfigMapper;
    }
}
