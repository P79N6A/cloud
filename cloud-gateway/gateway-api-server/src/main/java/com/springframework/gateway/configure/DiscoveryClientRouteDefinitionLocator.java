package com.springframework.gateway.configure;

import com.google.common.collect.Sets;
import com.springframework.gateway.common.thread.CloudGatewayThreadFactory;
import com.springframework.gateway.domain.dto.RouteConfigDTO;
import com.springframework.gateway.domain.entity.RouteConfig;
import com.springframework.gateway.domain.service.RouteConfigService;
import com.springframework.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.style.ToStringCreator;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.*;
import java.util.concurrent.*;

/**
 * 仅限同一服务注册发现治理中心下服务调用网关路由（针对跨中心请参考其他动态路由实现）
 *
 * @author summer  基于内存-redis-mysql-discovery方式做动态路由
 * 2018/7/3
 */
@Slf4j
public class DiscoveryClientRouteDefinitionLocator implements RouteDefinitionRepository {

    private TimeUnit rebuildIntervalTimeUnit = TimeUnit.SECONDS;
    private TimeUnit scanIntervalTimeUnit = TimeUnit.SECONDS;
    private ScheduledExecutorService scheduledExecutorService;
    private ExecutorService executorService;
    private ApplicationEventPublisher publisher;
    private DiscoveryClient discoveryClient;
    private DiscoveryLocatorProperties properties;
    private String routeIdPrefix;
    private RouteConfigService routeConfigService;

    @Value("${scan.interval:10}")
    private int scanInterval;
    /**
     * 本地缓存->redis->mysql
     */
    private final Map<String, RouteConfigDTO> routes = Collections.synchronizedMap(new LinkedHashMap<>());
    private Set<String> oldServiceIds = Collections.synchronizedSet(new HashSet<>());

    public DiscoveryClientRouteDefinitionLocator(ApplicationEventPublisher publisher,DiscoveryClient discoveryClient, DiscoveryLocatorProperties properties, RouteConfigService routeConfigService) {
        this.discoveryClient = discoveryClient;
        this.properties = properties;
        this.routeConfigService = routeConfigService;
        this.publisher = publisher;
        init(properties);
    }

    private void init(DiscoveryLocatorProperties properties) {
        if (StringUtils.hasText(properties.getRouteIdPrefix())) {
            this.routeIdPrefix = properties.getRouteIdPrefix();
        } else {
            this.routeIdPrefix = this.discoveryClient.getClass().getSimpleName() + "_";
        }
        this.scheduledExecutorService = new ScheduledThreadPoolExecutor(1, CloudGatewayThreadFactory
                .create("discoveryClientScanService", true));
        this.executorService = Executors.newSingleThreadExecutor(CloudGatewayThreadFactory.create("CachingRouteLocatorRefreshService", true));
    }
    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        scheduledExecutorService.scheduleWithFixedDelay(this::scan,scanInterval,
                scanInterval, scanIntervalTimeUnit);
    }

    /**
     * RoutePredicateHandlerMapping 使用 CachingRouteLocator 来获取 Route 信息。
     * 在 Spring Cloud Gateway 启动后，如果有新加入的服务，则需要刷新 CachingRouteLocator 缓存。
     * 这里有一点需要注意下 ：新加入的服务，指的是新的 serviceId ，而不是原有服务新增的实例。
     *  DiscoveryClient 获取服务列表，若发现变化，刷新 CachingRouteLocator 缓存。
     */
    private void scan(){
        executorService.submit(() -> {
            try {
                final List<String> discoveryClientServiceIds = discoveryClient.getServices();
                final Sets.SetView<String> difference = Sets.difference(oldServiceIds, new HashSet<>(discoveryClientServiceIds));
                if(!difference.isEmpty()){
                    oldServiceIds.clear();
                    oldServiceIds = new HashSet<>(discoveryClientServiceIds);
                    this.publisher.publishEvent(new RefreshRoutesEvent(this));
                }
            } catch (Throwable e) {
                //ignore
            }
        });
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
                .filter(instances -> !instances.isEmpty())
                .map(instances -> instances.get(0))
                .filter(instance -> {
                    Boolean include = includeExpr.getValue(evalCtxt, instance, Boolean.class);
                    if (include == null) {
                        return false;
                    }
                    return include;
                })
                .map(instance -> {
                    String serviceId = instance.getServiceId();
                    RouteDefinition routeDefinition = new RouteDefinition();
                    RouteConfigDTO routeConfig = routes.get(serviceId);
                    if (Objects.nonNull(routeConfig) && routeConfig.getStatus()) {
                        routeConfig = routeConfigService.findRouteConfig(serviceId);
                    }
                    //状态有效
                    if (Objects.nonNull(routeConfig) && routeConfig.getStatus()) {
                        routeDefinition.setId(routeConfig.getRouteId());
                        routeDefinition.setOrder(routeConfig.getOrder());
                        routeDefinition.setUri(URI.create(routeConfig.getUri()));
                        routeDefinition.setPredicates(routeConfig.getPredicateList());
                        routeDefinition.setFilters(routeConfig.getFilterList());
                    } else {
                        routeDefinition.setId(serviceId);
                        String uri = urlExpr.getValue(evalCtxt, instance, String.class);
                        if (!StringUtils.isEmpty(uri)) {
                            routeDefinition.setUri(URI.create(uri));
                        }
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
                        final RouteConfig config = saveOrUpdateRouteConfig(routeDefinition, serviceId);
                        log.info("保存路由配置数量为{},参数{}，服务id{}", config, routeDefinition, serviceId);
                    }
                    return routeDefinition;
                });
    }

    private RouteConfig saveOrUpdateRouteConfig(RouteDefinition routeDefinition, String serviceId) {
        RouteConfig routeConfig = new RouteConfig();
        routeConfig.setId(null);
        routeConfig.setRouteId(routeDefinition.getId());
        routeConfig.setFilters(JsonUtils.writeObjectToJson(routeDefinition.getFilters()));
        routeConfig.setPredicates(JsonUtils.writeObjectToJson(routeDefinition.getPredicates()));
        routeConfig.setStatus(true);
        routeConfig.setServiceId(serviceId);
        routeConfig.setServiceName(serviceId);
        routeConfig.setUri(routeDefinition.getUri() + "");
        routeConfig.setOperator(DiscoveryClientRouteDefinitionLocator.class.getSimpleName());
        routes.put(serviceId, covertToRouteConfigDTO(routeConfig));
        routeConfigService.saveRouteConfig(routeConfig);
        return routeConfig;
    }

    private String getValueFromExpr(SimpleEvaluationContext evalCtxt, SpelExpressionParser parser, ServiceInstance instance, Map.Entry<String, String> entry) {
        Expression valueExpr = parser.parseExpression(entry.getValue());
        return valueExpr.getValue(evalCtxt, instance, String.class);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.flatMap(r -> {
            RouteConfigDTO routeConfig = covertToRouteConfig(r);
            routes.put(r.getId(), routeConfig);
            routeConfigService.saveRouteConfig(routeConfig);
            return Mono.empty();
        });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap(id -> {
            if (routes.containsKey(id)) {
                routes.remove(id);
                routeConfigService.deleteRouteConfigByRouteId(id);
                return Mono.empty();
            }
            return Mono.defer(() -> Mono.error(new NotFoundException("RouteDefinition not found: " + routeId)));
        });
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

    private RouteConfigDTO covertToRouteConfigDTO(RouteConfig routeConfig) {
        ModelMapper mapper = new ModelMapper();
        final RouteConfigDTO routeConfigDTO = mapper.map(routeConfig, RouteConfigDTO.class);
        routeConfigDTO.setPredicateList(JsonUtils.readJsonToObjectList(PredicateDefinition.class,routeConfig.getPredicates()));
        routeConfigDTO.setFilterList(JsonUtils.readJsonToObjectList(FilterDefinition.class,routeConfig.getFilters()));
        return routeConfigDTO;
    }

    private RouteConfigDTO covertToRouteConfig(RouteDefinition routeDefinition) {
        RouteConfigDTO routeConfigDTO = new RouteConfigDTO();
        routeConfigDTO.setFilterList(routeDefinition.getFilters());
        routeConfigDTO.setPredicateList(routeDefinition.getPredicates());
        routeConfigDTO.setPredicates(JsonUtils.writeObjectToJson(routeDefinition.getPredicates()));
        routeConfigDTO.setFilters(JsonUtils.writeObjectToJson(routeDefinition.getFilters()));
        routeConfigDTO.setRouteId(routeDefinition.getId());
        routeConfigDTO.setOrder(routeDefinition.getOrder());
        routeConfigDTO.setStatus(true);
        routeConfigDTO.setUri(routeDefinition.getUri().toString());
        return routeConfigDTO;
    }

}
