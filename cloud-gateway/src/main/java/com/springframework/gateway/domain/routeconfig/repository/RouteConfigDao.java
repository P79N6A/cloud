package com.springframework.gateway.domain.routeconfig.repository;

import com.springframework.gateway.domain.routeconfig.dto.RouteConfigDTO;
import com.springframework.gateway.domain.routeconfig.entity.RouteConfig;
import org.springframework.cloud.gateway.route.RouteDefinition;

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
    List<RouteDefinition> findAll();

    /**
     * 查询
     *
     * @param serviceId
     * @return
     */
    RouteConfigDTO findRouteConfig(String serviceId);

    /**查询
     * @param serviceId
     * @param status
     * @return
     */
    RouteConfigDTO findRouteConfig(String serviceId, Boolean status);
}
