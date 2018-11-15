package com.springframework.gateway.domain.service;

import com.springframework.gateway.domain.dto.RouteConfigDTO;
import com.springframework.gateway.domain.entity.RouteConfig;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.util.List;

/**
 * @author summer
 * 2018/7/2
 */
public interface RouteConfigService {
    /**
     * 保存路由配置
     *
     * @param routeConfig
     * @return
     */
    boolean save(RouteConfig routeConfig);

    /**
     * 查询所有路由配置
     *
     * @return
     */
    List<RouteDefinition> findAll();


    /**查询路由
     * @param serviceId 服务id
     * @return
     */
    RouteConfigDTO findRouteConfig(String serviceId);
}
