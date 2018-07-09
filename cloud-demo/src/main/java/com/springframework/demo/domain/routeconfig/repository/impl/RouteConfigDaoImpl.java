package com.springframework.demo.domain.routeconfig.repository.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.springframework.demo.domain.routeconfig.entity.RouteConfig;
import com.springframework.demo.domain.routeconfig.mapper.RouteConfigMapper;
import com.springframework.demo.domain.routeconfig.repository.RouteConfigDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author summer
 * 2018/7/2
 */
@Service
public class RouteConfigDaoImpl extends ServiceImpl<RouteConfigMapper, RouteConfig> implements RouteConfigDao {

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
    public RouteConfig findRouteConfig(String serviceId) {
        final List<RouteConfig> list = routeConfigMapper.selecRouteConfigtList(serviceId);
        return CollectionUtils.isEmpty(list)?null:list.get(0);
    }
    @Override
    public RouteConfig findRouteConfig(String serviceId,Boolean status) {
        EntityWrapper<RouteConfig> wrapper = new EntityWrapper<>();
        final List<RouteConfig> list = routeConfigMapper.selectList(wrapper);
        return CollectionUtils.isEmpty(list)? null: list.get(0);
    }
}
