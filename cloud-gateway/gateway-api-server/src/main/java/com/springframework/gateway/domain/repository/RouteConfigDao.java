package com.springframework.gateway.domain.repository;

import com.springframework.gateway.domain.dto.RouteConfigDTO;
import com.springframework.gateway.domain.entity.RouteConfig;
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
    boolean saveRouteConfig(RouteConfig routeConfig);

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
