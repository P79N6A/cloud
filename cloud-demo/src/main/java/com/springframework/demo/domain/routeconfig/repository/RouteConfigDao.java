package com.springframework.demo.domain.routeconfig.repository;


import com.springframework.demo.domain.routeconfig.entity.RouteConfig;

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
    Integer save(RouteConfig routeConfig);

    /**
     * @return
     */

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
}
