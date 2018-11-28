package com.springframework.gateway.web;

import com.springframework.gateway.domain.entity.RouteConfig;
import com.springframework.gateway.domain.service.RouteConfigService;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author summer
 * 2018/11/28
 */
//@Api(value = "路由配置", description = "路由配置接口")
@RestController
@RequestMapping("/route/config")
public class RouteConfigController {

    private final RouteConfigService routeConfigService;
    private final RouteDefinitionLocator routeDefinitionLocator;
    private final ApplicationEventPublisher publisher;


    public RouteConfigController(RouteConfigService routeConfigService, RouteDefinitionLocator routeDefinitionLocator, ApplicationEventPublisher publisher) {
        this.routeConfigService = routeConfigService;
        this.routeDefinitionLocator = routeDefinitionLocator;
        this.publisher = publisher;
    }

    @GetMapping
//    @ApiOperation(value = "查询所有路由配置", notes = "查询所有路由配置", response = RouteConfig.class)
    public List<RouteConfig> routeConfig() {
        return routeConfigService.listAllRouteConfig();
    }

    @GetMapping("/serviceId/{serviceId}")
//    @ApiOperation(value = "根据serviceId查询路由配置", notes = "根据serviceId查询路由配置", response = RouteConfig.class)
    public RouteConfig getRouteConfigByServiceId(@PathVariable("serviceId") String serviceId) {
        return routeConfigService.getRouteConfigByServiceId(serviceId);
    }

    @GetMapping("/{id}")
//    @ApiOperation(value = "根据Id查询路由配置", notes = "根据Id查询路由配置", response = RouteConfig.class)
    public RouteConfig getRouteConfigById(@PathVariable("id") Long id) {
        return routeConfigService.getRouteConfigById(id);
    }

    @DeleteMapping("/{id}")
//    @ApiOperation(value = "根据Id删除路由配置", notes = "根据Id删除路由配置")
    public void delRouteConfigById(@PathVariable("id") Long id) {
        routeConfigService.delRouteConfigById(id);
    }

}
