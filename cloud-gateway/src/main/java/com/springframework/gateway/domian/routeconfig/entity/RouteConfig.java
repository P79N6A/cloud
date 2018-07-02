package com.springframework.gateway.domian.routeconfig.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author summer
 * 2018/7/2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("route_config")
public class RouteConfig {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("service_name")
    private String serviceName;
    @TableField("service_id")
    private String serviceId;
    @TableField("uri")
    private String uri;
    @TableField("order")
    private Integer order;
    /**
     * 多个predicate,用逗号分隔
     */
    @TableField("predicates")
    private String predicates;

}
