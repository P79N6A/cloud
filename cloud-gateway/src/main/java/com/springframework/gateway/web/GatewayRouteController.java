package com.springframework.gateway.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.actuate.GatewayControllerEndpoint;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.cloud.gateway.route.*;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.core.Ordered;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author summer
 * 2018/7/4
 */
@RestController
@RequestMapping("/gateway")
@Slf4j
public class GatewayRouteController  implements ApplicationEventPublisherAware {

    private RouteDefinitionLocator routeDefinitionLocator;
    private List<GlobalFilter> globalFilters;
    private List<GatewayFilterFactory> gatewayFilters;
    private RouteDefinitionWriter routeDefinitionWriter;
    private RouteLocator routeLocator;
    private ApplicationEventPublisher publisher;

    public GatewayRouteController(RouteDefinitionLocator routeDefinitionLocator, List<GlobalFilter> globalFilters, List<GatewayFilterFactory> gatewayFilters, RouteDefinitionWriter routeDefinitionWriter, RouteLocator routeLocator, ApplicationEventPublisher publisher) {
        this.routeDefinitionLocator = routeDefinitionLocator;
        this.globalFilters = globalFilters;
        this.gatewayFilters = gatewayFilters;
        this.routeDefinitionWriter = routeDefinitionWriter;
        this.routeLocator = routeLocator;
        this.publisher = publisher;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    // TODO: Add uncommited or new but not active routes endpoint

    @PostMapping("/refresh")
    public Mono<Void> refresh() {
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        return Mono.empty();
    }
    @GetMapping("/globalfilters")
    public Mono<HashMap<String, Object>> globalfilters() {
        return getNamesToOrders(this.globalFilters);
    }
    @GetMapping("/routefilters")
    public Mono<HashMap<String, Object>> routefilers() {
        return getNamesToOrders(this.gatewayFilters);
    }

    private <T> Mono<HashMap<String, Object>> getNamesToOrders(List<T> list) {
        return Flux.fromIterable(list).reduce(new HashMap<>(), this::putItem);
    }

    private HashMap<String, Object> putItem(HashMap<String, Object> map, Object o) {
        Integer order = null;
        if (o instanceof Ordered) {
            order = ((Ordered) o).getOrder();
        }
        //filters.put(o.getClass().getName(), order);
        map.put(o.toString(), order);
        return map;
    }
    // TODO: Flush out routes without a definition
    @GetMapping("/routes")
    public Mono<List<Map<String, Object>>> routes() {
        Mono<Map<String, RouteDefinition>> routeDefs = this.routeDefinitionLocator.getRouteDefinitions()
                .collectMap(RouteDefinition::getId);
        Mono<List<Route>> routes = this.routeLocator.getRoutes().collectList();
        return Mono.zip(routeDefs, routes).map(tuple -> {
            Map<String, RouteDefinition> defs = tuple.getT1();
            List<Route> routeList = tuple.getT2();
            List<Map<String, Object>> allRoutes = new ArrayList<>();

            routeList.forEach(route -> {
                HashMap<String, Object> r = new HashMap<>();
                r.put("route_id", route.getId());
                r.put("order", route.getOrder());

                if (defs.containsKey(route.getId())) {
                    r.put("route_definition", defs.get(route.getId()));
                } else {
                    HashMap<String, Object> obj = new HashMap<>();

                    obj.put("predicate", route.getPredicate().toString());

                    if (!route.getFilters().isEmpty()) {
                        ArrayList<String> filters = new ArrayList<>();
                        for (GatewayFilter filter : route.getFilters()) {
                            filters.add(filter.toString());
                        }

                        obj.put("filters", filters);
                    }

                    if (!obj.isEmpty()) {
                        r.put("route_object", obj);
                    }
                }
                allRoutes.add(r);
            });

            return allRoutes;
        });
    }

    /*
    http POST :8080/admin/gateway/routes/apiaddreqhead uri=http://httpbin.org:80 predicates:='["Host=**.apiaddrequestheader.org", "Path=/headers"]' filters:='["AddRequestHeader=X-Request-ApiFoo, ApiBar"]'
    */
    @PostMapping("/routes/{id}")
    @SuppressWarnings("unchecked")
    public Mono<ResponseEntity<Void>> save(@PathVariable String id, @RequestBody Mono<RouteDefinition> route) {
        return this.routeDefinitionWriter.save(route.map(r -> {
            r.setId(id);
            log.debug("Saving route: " + route);
            return r;
        })).then(Mono.defer(() ->
                Mono.just(ResponseEntity.created(URI.create("/routes/" + id)).build())
        ));
    }

    @DeleteMapping("/routes/{id}")
    public Mono<ResponseEntity<Object>> delete(@PathVariable String id) {
        return this.routeDefinitionWriter.delete(Mono.just(id))
                .then(Mono.defer(() -> Mono.just(ResponseEntity.ok().build())))
                .onErrorResume(t -> t instanceof NotFoundException, t -> Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping("/routes/{id}")
    public Mono<ResponseEntity<RouteDefinition>> route(@PathVariable String id) {
        //TODO: missing RouteLocator
        return this.routeDefinitionLocator.getRouteDefinitions()
                .filter(route -> route.getId().equals(id))
                .singleOrEmpty()
                .map(route -> ResponseEntity.ok(route))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping("/routes/{id}/combinedfilters")
    public Mono<HashMap<String, Object>> combinedfilters(@PathVariable String id) {
        //TODO: missing global filters
        return this.routeLocator.getRoutes()
                .filter(route -> route.getId().equals(id))
                .reduce(new HashMap<>(), this::putItem);
    }
}