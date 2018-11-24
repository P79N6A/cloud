package com.springframework.gateway.domain.repository;

import com.springframework.gateway.domain.entity.RouteConfig;

import java.util.List;

/**
 * @author summer
 * 2018/7/2
 */
public interface RouteConfigDao {
    /**
     * 保存路由配置
     *
     * @param routeConfig
     * @return
     */
    boolean saveRouteConfig(RouteConfig routeConfig);

    /**
     * @return
     */
    List<RouteConfig> findAll();

    /**
     * 查询
     *
     * @param serviceId
     * @return
     */
    RouteConfig findRouteConfig(String serviceId);

    /**查询
     * @param serviceId
     * @param status
     * @return
     */
    RouteConfig findRouteConfig(String serviceId, Boolean status);

    /** 根据 routeId删除配置（逻辑删除）
     * @param routeId
     * @return
     */
    boolean deleteRouteConfigByRouteId(String routeId);
}
