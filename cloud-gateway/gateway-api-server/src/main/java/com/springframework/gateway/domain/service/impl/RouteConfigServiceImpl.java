package com.springframework.gateway.domain.service.impl;

import com.springframework.gateway.domain.dto.RouteConfigDTO;
import com.springframework.gateway.domain.entity.RouteConfig;
import com.springframework.gateway.domain.repository.impl.RouteConfigDaoImpl;
import com.springframework.gateway.domain.service.RouteConfigCacheService;
import com.springframework.gateway.domain.service.RouteConfigService;
import com.springframework.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author summer
 * 2018/7/2
 */
@Service("routeConfigService")
@Transactional(readOnly = true, rollbackFor = Exception.class)
@Slf4j
public class RouteConfigServiceImpl implements RouteConfigService {
    private RouteConfigDaoImpl routeConfigDao;
    private RouteConfigCacheService configCacheService;

    @Autowired
    public RouteConfigServiceImpl(RouteConfigCacheService configCacheService, RouteConfigDaoImpl routeConfigDao) {
        this.configCacheService = configCacheService;
        this.routeConfigDao = routeConfigDao;
    }


    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean saveRouteConfig(RouteConfig routeConfig) {
        routeConfigDao.saveRouteConfig(routeConfig);
        configCacheService.saveRouteConfigCache(routeConfig);
        return true;
    }

    /**
     * 保存路由配置
     *
     * @param routeConfigDTO
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean saveRouteConfig(RouteConfigDTO routeConfigDTO) {
        RouteConfig routeConfig = this.covertByRouteConfigDTO(routeConfigDTO);
        return saveRouteConfig(routeConfig);
    }

    @Override
    public List<RouteDefinition> listRouteDefinition() {
        final List<RouteConfig> routeConfigList = routeConfigDao.findAll();
        if (!CollectionUtils.isEmpty(routeConfigList)) {
            List<RouteConfigDTO> dtoList = new ArrayList<>(10);
            routeConfigList.parallelStream().forEach(routeConfig -> {
                dtoList.add(covertToRouteConfigDTO(routeConfig));
            });
            if (!CollectionUtils.isEmpty(dtoList)) {
                List<RouteDefinition> routeDefinitionList = new ArrayList<>(10);
                dtoList.parallelStream().forEach(routeConfig -> {
                    RouteDefinition routeDefinition = new RouteDefinition();
                    routeDefinitionList.add(routeDefinition);
                    routeDefinition.setUri(URI.create(routeConfig.getUri()));
                    routeDefinition.setId(routeConfig.getServiceId());
                    routeDefinition.setOrder(routeConfig.getOrders());
                    routeDefinition.setFilters(getFilterList(routeConfig));
                    routeDefinition.setPredicates(getPredicateList(routeConfig));
                });
                return routeDefinitionList;
            }
        }
        return null;
    }

    @Override
    public RouteConfigDTO findRouteConfig(@NotEmpty String serviceId) {
        RouteConfig routeConfig = getRouteConfigByServiceId(serviceId);
        return covertToRouteConfigDTO(routeConfig);
    }

    /**
     * 根据routeId 删除配置（逻辑删除）
     *
     * @param routeId
     * @return
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean deleteRouteConfigByRouteId(String routeId) {
        routeConfigDao.deleteRouteConfigByRouteId(routeId);
        configCacheService.expireRouteConfigCache(routeId);
        return true;
    }

    @Override
    public RouteConfigDTO covertToRouteConfigDTO(RouteConfig routeConfig) {
        if (routeConfig == null) {
            return null;
        }
        ModelMapper mapper = new ModelMapper();
        RouteConfigDTO routeConfigDTO = mapper.map(routeConfig, RouteConfigDTO.class);
        routeConfigDTO.setPredicateList(JsonUtils.readJsonToObjectList(PredicateDefinition.class, routeConfig.getPredicates()));
        routeConfigDTO.setFilterList(JsonUtils.readJsonToObjectList(FilterDefinition.class, routeConfig.getFilters()));
        return routeConfigDTO;
    }

    /**
     * @return 查询所有路由配置
     */
    @Override
    public List<RouteConfig> listAllRouteConfig() {
        return routeConfigDao.findAll();
    }

    /**
     * @param serviceId 根据serviceId查询
     * @return
     */
    @Override
    public RouteConfig getRouteConfigByServiceId(@NotEmpty String serviceId) {
        RouteConfig routeConfig = configCacheService.findRouteConfigCache(serviceId);
        if (routeConfig == null) {
            routeConfig = routeConfigDao.findRouteConfig(serviceId);
            Optional.ofNullable(routeConfig).ifPresent(r -> {
                configCacheService.saveRouteConfigCache(r);
            });
        }
        return routeConfig;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public RouteConfig getRouteConfigById(@NotNull Long id) {
        RouteConfig routeConfig = new RouteConfig();
        routeConfig.setId(id);
        return routeConfig.selectById();
    }

    /**
     * @param id
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void delRouteConfigById(Long id) {
        RouteConfig routeConfig = new RouteConfig();
        routeConfig.setId(id);
        final RouteConfig config = routeConfig.selectById();
        configCacheService.expireRouteConfigCache(config.getRouteId());
        routeConfig.deleteById();
    }

    /**
     * @return 过滤器
     */
    private List<FilterDefinition> getFilterList(RouteConfigDTO routeConfig) {
        if (StringUtils.isNotBlank(routeConfig.getFilters())) {
            if (log.isDebugEnabled()) {
                log.debug("路由参数{}", routeConfig.getFilters());
            }
            return JsonUtils.readJsonToObjectList(FilterDefinition.class, routeConfig.getFilters());
        }
        return null;
    }

    /**
     * @return 获取路由 PredicateDefinition{name='Path', args={pattern=/demo-server/**}}
     */
    private List<PredicateDefinition> getPredicateList(RouteConfigDTO routeConfig) {
        if (StringUtils.isNotBlank(routeConfig.getPredicates())) {
            if (log.isDebugEnabled()) {
                log.debug("路由参数{}", routeConfig.getPredicates());
            }
            return JsonUtils.readJsonToObjectList(PredicateDefinition.class, routeConfig.getPredicates());
        }
        return null;
    }

    private RouteConfig covertByRouteConfigDTO(RouteConfigDTO routeConfigDTO) {
        RouteConfig routeConfig = new RouteConfig();
        routeConfig.setStatus(routeConfigDTO.getStatus());
        routeConfig.setFilters(routeConfigDTO.getFilters());
        routeConfig.setPredicates(routeConfigDTO.getPredicates());
        routeConfig.setRouteId(routeConfigDTO.getRouteId());
        routeConfig.setId(null);
        routeConfig.setOperator(routeConfigDTO.getOperator());
        routeConfig.setServiceId(routeConfigDTO.getServiceId());
        routeConfig.setOrders(routeConfigDTO.getOrders());
        routeConfig.setServiceName(routeConfigDTO.getServiceName());
        routeConfig.setUri(routeConfigDTO.getUri());
        routeConfig.setCreatedBy(routeConfigDTO.getCreatedBy());
        routeConfig.setLastModifiedBy(routeConfigDTO.getOperator());
        return routeConfig;
    }
}
