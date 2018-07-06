package com.springframework.gateway.config;

import com.springframework.gateway.domain.routeconfig.entity.RouteConfig;
import com.springframework.gateway.domain.routeconfig.service.RouteConfigService;
import jdk.nashorn.internal.runtime.options.Option;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.style.ToStringCreator;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * @author summer 基于eureka 服务发现 动态路由
 * 2018/7/4
 */
@Slf4j
public class DiscoveryClientRouteDefinitionLocator implements RouteDefinitionRepository {

    private final DiscoveryClient discoveryClient;
    private final DiscoveryLocatorProperties properties;
    private final String routeIdPrefix;
    private final RouteConfigService routeConfigService;

    public DiscoveryClientRouteDefinitionLocator(RouteConfigService routeConfigService, DiscoveryClient discoveryClient, DiscoveryLocatorProperties properties) {
        this.discoveryClient = discoveryClient;
        this.properties = properties;
        this.routeConfigService = routeConfigService;
        if (StringUtils.hasText(properties.getRouteIdPrefix())) {
            this.routeIdPrefix = properties.getRouteIdPrefix();
        } else {
            this.routeIdPrefix = this.discoveryClient.getClass().getSimpleName() + "_";
        }
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        SimpleEvaluationContext evalCtxt = SimpleEvaluationContext
                .forReadOnlyDataBinding()
                .withInstanceMethods()
                .build();

        SpelExpressionParser parser = new SpelExpressionParser();
        Expression includeExpr = parser.parseExpression(properties.getIncludeExpression());
        Expression urlExpr = parser.parseExpression(properties.getUrlExpression());

        return Flux.fromIterable(discoveryClient.getServices())
                .map(discoveryClient::getInstances)
                .filter(instances -> {
                    return !instances.isEmpty();
                })
                .map(instances -> instances.get(0))
                .filter(instance -> {
                    Boolean include = includeExpr.getValue(evalCtxt, instance, Boolean.class);
                    if (include == null) {
                        return false;
                    }
                    String serviceId = instance.getServiceId();
                    RouteConfig routeConfig = routeConfigService.findRouteConfig(serviceId);
                    //状态有效
                    if (Optional.ofNullable(routeConfig).isPresent() && !routeConfig.getStatus()) {
                        return false;
                    }
                    return include;
                })
                .map(instance -> {
                    String serviceId = instance.getServiceId();
                    RouteDefinition routeDefinition = new RouteDefinition();
                    RouteConfig routeConfig = routeConfigService.findRouteConfig(serviceId);
                    //状态有效
                    if (Optional.ofNullable(routeConfig).isPresent() && routeConfig.getStatus()) {
                        routeDefinition.setId(routeConfig.getRouteId());
                        routeDefinition.setOrder(routeConfig.getOrder());
                        routeDefinition.setUri(URI.create(routeConfig.getUri()));
                        routeDefinition.setPredicates(routeConfig.getPredicateList());
                        routeDefinition.setFilters(routeConfig.getFilterList());
                    } else {
                        routeDefinition.setId(this.routeIdPrefix + serviceId);
                        String uri = urlExpr.getValue(evalCtxt, instance, String.class);
                        routeDefinition.setUri(URI.create(uri));

                        final ServiceInstance instanceForEval = new DiscoveryClientRouteDefinitionLocator.DelegatingServiceInstance(instance, properties);

                        for (PredicateDefinition original : this.properties.getPredicates()) {
                            PredicateDefinition predicate = new PredicateDefinition();
                            predicate.setName(original.getName());
                            for (Map.Entry<String, String> entry : original.getArgs().entrySet()) {
                                String value = getValueFromExpr(evalCtxt, parser, instanceForEval, entry);
                                predicate.addArg(entry.getKey(), value);
                            }
                            routeDefinition.getPredicates().add(predicate);
                        }

                        for (FilterDefinition original : this.properties.getFilters()) {
                            FilterDefinition filter = new FilterDefinition();
                            filter.setName(original.getName());
                            for (Map.Entry<String, String> entry : original.getArgs().entrySet()) {
                                String value = getValueFromExpr(evalCtxt, parser, instanceForEval, entry);
                                filter.addArg(entry.getKey(), value);
                            }
                            routeDefinition.getFilters().add(filter);
                        }
                        final Integer saveRouteConfig = saveRouteConfig(routeDefinition, serviceId);
                    }
                    return routeDefinition;
                });
    }

    private Integer saveRouteConfig(RouteDefinition routeDefinition, String serviceId) {
        RouteConfig routeConfig = new RouteConfig();
        Date curr = new Date(Instant.now().getEpochSecond());
        StringBuilder filtersSts = new StringBuilder();
        StringBuilder predicateListStr = new StringBuilder();
        List<FilterDefinition> filters = null;
        List<PredicateDefinition> predicateList = null;
        filters = routeDefinition.getFilters();
        filters.forEach(e -> {
            filtersSts.append(e.toString()).append("-");
        });
        predicateList = routeDefinition.getPredicates();
        predicateList.forEach(e -> {
            predicateListStr.append(e.toString()).append("-");
        });
        routeConfig.setRouteId(routeDefinition.getId());
        routeConfig.setCreateTime(curr);
        routeConfig.setUpdateTime(curr);
        routeConfig.setFilters(filtersSts.toString());
        routeConfig.setPredicates(predicateListStr.toString());
        routeConfig.setStatus(true);
        routeConfig.setServiceId(serviceId);

        Integer result = routeConfigService.save(routeConfig);
        if (Optional.ofNullable(result).isPresent()) {
            return result;
        }
        return 0;
    }

    String getValueFromExpr(SimpleEvaluationContext evalCtxt, SpelExpressionParser parser, ServiceInstance instance, Map.Entry<String, String> entry) {
        Expression valueExpr = parser.parseExpression(entry.getValue());
        return valueExpr.getValue(evalCtxt, instance, String.class);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }

    private static class DelegatingServiceInstance implements ServiceInstance {

        final ServiceInstance delegate;
        private final DiscoveryLocatorProperties properties;

        private DelegatingServiceInstance(ServiceInstance delegate, DiscoveryLocatorProperties properties) {
            this.delegate = delegate;
            this.properties = properties;
        }

        @Override
        public String getServiceId() {
            if (properties.isLowerCaseServiceId()) {
                return delegate.getServiceId().toLowerCase();
            }
            return delegate.getServiceId();
        }

        @Override
        public String getHost() {
            return delegate.getHost();
        }

        @Override
        public int getPort() {
            return delegate.getPort();
        }

        @Override
        public boolean isSecure() {
            return delegate.isSecure();
        }

        @Override
        public URI getUri() {
            return delegate.getUri();
        }

        @Override
        public Map<String, String> getMetadata() {
            return delegate.getMetadata();
        }

        @Override
        public String getScheme() {
            return delegate.getScheme();
        }

        @Override
        public String toString() {
            return new ToStringCreator(this)
                    .append("delegate", delegate)
                    .append("properties", properties)
                    .toString();
        }
    }
}
