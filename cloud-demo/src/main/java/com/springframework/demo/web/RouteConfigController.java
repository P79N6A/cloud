package com.springframework.demo.web;

import com.springframework.demo.config.RouteConfig;
import com.springframework.demo.domain.routeconfig.service.RouteConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author summer
 * 2018/7/2
 */
@RestController
@RequestMapping("/routeConfig")
@Api("路由配置")
public class RouteConfigController {
    private final RouteConfigService routeConfigService;

    @Autowired
    public RouteConfigController(RouteConfigService routeConfigService) {
        this.routeConfigService = routeConfigService;
    }

    @PostMapping("")
    @ApiOperation(value = "保存路由配置", notes = "保存路由配置信息")
    public Mono<RouteConfig> save(RouteConfig routeConfig) {
        return this.routeConfigService.save(routeConfig);
    }

    @GetMapping("")
    @ApiOperation(value = "查询路由配置", notes = "查询路由配置信息")
    public Flux<RouteConfig> findAll() {
        return this.routeConfigService.findAll();
    }
}
