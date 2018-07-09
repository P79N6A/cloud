package com.springframework.gateway.domain.routeconfig.repository.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.springframework.gateway.domain.routeconfig.dto.RouteConfigDTO;
import com.springframework.gateway.domain.routeconfig.entity.RouteConfig;
import com.springframework.gateway.domain.routeconfig.mapper.RouteConfigMapper;
import com.springframework.gateway.domain.routeconfig.repository.RouteConfigDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

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

    @Override
    public Integer save(RouteConfig routeConfig) {
        return routeConfigMapper.insert(routeConfig);
    }

    @Override
    public List<RouteDefinition> findAll() {
        return null;
    }

    @Override
    public RouteConfigDTO findRouteConfig(String serviceId) {
        final List<RouteConfigDTO> list = routeConfigMapper.selecRouteConfigtList(serviceId);
        return CollectionUtils.isEmpty(list)?null:list.get(0);
    }
    @Override
    public RouteConfigDTO findRouteConfig(String serviceId,Boolean status) {
        EntityWrapper<RouteConfig> wrapper = new EntityWrapper<>();
        wrapper.eq(RouteConfig.SERVICE_ID, serviceId);
        wrapper.eq(RouteConfig.STATUS, status);
        final List<RouteConfig> list = routeConfigMapper.selectList(wrapper);
        return null;
    }
}
