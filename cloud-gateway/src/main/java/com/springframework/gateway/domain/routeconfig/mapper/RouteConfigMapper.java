package com.springframework.gateway.domain.routeconfig.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.springframework.gateway.domain.routeconfig.dto.RouteConfigDTO;
import com.springframework.gateway.domain.routeconfig.entity.RouteConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author summer
 * @since 2018-07-03
 */
@Mapper
@Repository
public interface RouteConfigMapper extends BaseMapper<RouteConfig> {

    /**
     * 查询
     *
     * @param serviceId
     * @return
     */
    @Select("select * from route_config where service_id = #{serviceId} limit 1000")
    List<RouteConfigDTO> selecRouteConfigtList(@Param("serviceId") String serviceId);

}