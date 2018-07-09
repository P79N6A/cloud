package com.springframework.demo.domain.routeconfig.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.springframework.demo.domain.routeconfig.entity.RouteConfig;
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
@Repository
@Mapper
public interface RouteConfigMapper extends BaseMapper<RouteConfig> {

    @Select("select * from route_config where service_id = #{serviceId} limit 1000")
    List<RouteConfig> selecRouteConfigtList(@Param("serviceId") String serviceId);

}