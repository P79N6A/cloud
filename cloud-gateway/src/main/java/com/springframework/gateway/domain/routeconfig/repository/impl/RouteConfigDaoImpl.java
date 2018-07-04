package com.springframework.gateway.domain.routeconfig.repository.impl;

import com.springframework.gateway.domain.routeconfig.mapper.RouteConfigMapper;
import com.springframework.gateway.domain.routeconfig.repository.RouteConfigDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * @author summer
 * 2018/7/2
 */
@Service
public class RouteConfigDaoImpl implements RouteConfigDao {

    private final RouteConfigMapper routeConfigMapper;

    @Autowired
    public RouteConfigDaoImpl(RouteConfigMapper routeConfigMapper) {
        this.routeConfigMapper = routeConfigMapper;
    }
}
