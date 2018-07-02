package com.springframework.gateway.web;

import com.springframework.gateway.domian.routeconfig.entity.RouteConfig;
import com.springframework.gateway.domian.routeconfig.service.RouteConfigService;
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
public class RouteConfigController {
    private final RouteConfigService routeConfigService;

    @Autowired
    public RouteConfigController(RouteConfigService routeConfigService) {
        this.routeConfigService = routeConfigService;
    }

    @PostMapping("")
    public Mono<RouteConfig> save(RouteConfig routeConfig) {
        return this.routeConfigService.save(routeConfig);
    }

    @GetMapping("")
    public Flux<RouteConfig> findAll() {
        return this.routeConfigService.findAll();
    }
}
