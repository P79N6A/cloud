package com.springframework.gateway.domain.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springframework.gateway.domain.dto.RouteConfigDTO;
import com.springframework.gateway.domain.entity.RouteConfig;
import com.springframework.gateway.domain.mapper.RouteConfigMapper;
import com.springframework.gateway.domain.repository.RouteConfigDao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author summer
 * 2018/7/2
 */
@Repository("routeConfigDao")
public class RouteConfigDaoImpl extends ServiceImpl<RouteConfigMapper, RouteConfig> implements RouteConfigDao {

    private final RouteConfigMapper routeConfigMapper;

    @Autowired
    public RouteConfigDaoImpl(RouteConfigMapper routeConfigMapper) {
        this.routeConfigMapper = routeConfigMapper;
    }

    @Override
    public boolean saveRouteConfig(RouteConfig routeConfig) {
        if (this.save(routeConfig)) {
            return true;
        }
        throw new RuntimeException("保存失败");
    }

    @Override
    public List<RouteDefinition> findAll() {
//        QueryWrapper<RouteConfig> wrapper = new QueryWrapper<>();
//        final List<RouteConfig> routeConfigList = this.list(wrapper);
//        if (!CollectionUtils.isEmpty(routeConfigList)) {
//            List<RouteConfigDTO> dtoList = new ArrayList<>(10);
//            routeConfigList.parallelStream().forEach(routeConfig -> {
//                dtoList.add(covertDto(routeConfig));
//            });
//            if (!CollectionUtils.isEmpty(dtoList)) {
//                List<RouteDefinition> routeDefinitionList = new ArrayList<>(10);
//                dtoList.parallelStream().forEach(config -> {
//                    RouteDefinition routeDefinition = new RouteDefinition();
//                    routeDefinitionList.add(routeDefinition);
//                    BeanUtil.copyProperties(routeDefinition, config);
//                    routeDefinition.setFilters(config.getFilterList());
//                    routeDefinition.setPredicates(config.getPredicateList());
//                });
//                return routeDefinitionList;
//            }
//        }
        return null;
    }

    @Override
    public RouteConfigDTO findRouteConfig(String serviceId) {
//        QueryWrapper<RouteConfig> wrapper = new QueryWrapper<>();
//        wrapper.eq(RouteConfig.SERVICE_ID, serviceId);
//        final RouteConfig routeConfig = routeConfigMapper.selectOne(wrapper);
        return covertDto(null);
    }

    private RouteConfigDTO covertDto(RouteConfig routeConfig) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(routeConfig, RouteConfigDTO.class);
    }

    @Override
    public RouteConfigDTO findRouteConfig(String serviceId, Boolean status) {
//        QueryWrapper<RouteConfig> wrapper = new QueryWrapper<>();
//        wrapper.eq(RouteConfig.SERVICE_ID, serviceId);
//        wrapper.eq(RouteConfig.STATUS, status);
//        final RouteConfig routeConfig = this.getOne(wrapper, false);
        return covertDto(null);
    }
}
