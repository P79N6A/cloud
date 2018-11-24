package com.springframework.gateway.domain.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springframework.gateway.domain.dto.RouteConfigDTO;
import com.springframework.gateway.domain.entity.RouteConfig;
import com.springframework.gateway.domain.mapper.RouteConfigMapper;
import com.springframework.gateway.domain.repository.RouteConfigDao;
import com.springframework.utils.BeanUtil;
import org.apache.commons.collections.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author summer
 * 2018/7/2
 */
@Repository("routeConfigDao")
public class RouteConfigDaoImpl extends ServiceImpl<RouteConfigMapper, RouteConfig> implements RouteConfigDao {

    private final RouteConfigMapper routeConfigMapper;

    @Autowired
    public RouteConfigDaoImpl(RouteConfigMapper routeConfigMapper) {
        this.routeConfigMapper = routeConfigMapper;
    }

    @Override
    public boolean saveRouteConfig(RouteConfig routeConfig) {
        if (this.save(routeConfig)) {
            return true;
        }
        throw new RuntimeException("保存失败");
    }

    @Override
    public List<RouteConfig> findAll() {


        QueryWrapper<RouteConfig> wrapper = new QueryWrapper<>();
        final List<RouteConfig> routeConfigList = this.list(wrapper);
        return routeConfigList;
    }

    @Override
    public RouteConfig findRouteConfig(String serviceId) {
        QueryWrapper<RouteConfig> wrapper = new QueryWrapper<>();
        wrapper.eq(RouteConfig.SERVICE_ID, serviceId);
        final RouteConfig routeConfig = routeConfigMapper.selectOne(wrapper);
        return routeConfig;
    }

    @Override
    public RouteConfig findRouteConfig(String serviceId, Boolean status) {
        QueryWrapper<RouteConfig> wrapper = new QueryWrapper<>();
        wrapper.eq(RouteConfig.SERVICE_ID, serviceId);
        wrapper.eq(RouteConfig.STATUS, status);
        final RouteConfig routeConfig = this.getOne(wrapper, false);
        return routeConfig;
    }

    /**
     * 根据 routeId删除配置（逻辑删除）
     *
     * @param routeId
     * @return
     */
    @Override
    public boolean deleteRouteConfigByRouteId(String routeId) {
        UpdateWrapper<RouteConfig> wrapper = new UpdateWrapper<>();
        wrapper.eq(RouteConfig.ROUTE_ID, routeId);
        RouteConfig entity = new RouteConfig();
        entity.setStatus(false);
        if (!update(entity, wrapper)) {
            throw new RuntimeException("根据 routeId删除配置失败（逻辑删除）");
        }
        return true;
    }

}
