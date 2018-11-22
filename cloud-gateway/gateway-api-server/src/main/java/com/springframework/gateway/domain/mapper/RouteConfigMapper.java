package com.springframework.gateway.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springframework.gateway.domain.entity.RouteConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
public interface RouteConfigMapper extends BaseMapper<RouteConfig> {

    /**
     * 查询
     *
     * @param serviceId  服务id
     * @param limit 限制条数
     * @return
     */
    @Select("select * from route_config where service_id = #{serviceId} limit #{limit}")
    List<RouteConfig> selecRouteConfigtList(@Param("serviceId") String serviceId, @Param("limit") Integer limit);

}