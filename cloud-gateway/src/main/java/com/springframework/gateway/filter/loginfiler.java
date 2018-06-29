package com.springframework.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author summer
 * 2018/6/29
 */
public class loginfiler  implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, GatewayFilterChain gatewayFilterChain) {
        return gatewayFilterChain.filter(serverWebExchange.mutate().build());
    }
}
