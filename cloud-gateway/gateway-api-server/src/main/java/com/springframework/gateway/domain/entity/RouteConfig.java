package com.springframework.gateway.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.ibatis.reflection.MetaObject;

import java.io.Serializable;
import java.util.Date;

/**
 * @author summer
 * 2018/7/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("route_config")
public class RouteConfig extends Model<RouteConfig> {
    @TableId(value = "id", type = IdType.AUTO)
    protected Long id;
    /**
     * 逻辑删除 1删除 0未删除
     */
    @TableField("is_deleted")
    @TableLogic("0")
    protected Integer isDeleted = 0;

    @TableField(value = "created_by", strategy = FieldStrategy.NOT_NULL, fill = FieldFill.INSERT)
    protected String createdBy;
    @TableField(value = "created_time", update = "now()", strategy = FieldStrategy.NOT_NULL, fill = FieldFill.INSERT)
    protected Date createdTime;
    @TableField(value = "last_modified_by", strategy = FieldStrategy.NOT_NULL, fill = FieldFill.UPDATE)
    protected String lastModifiedBy;
    @TableField(value = "last_modified_time", update = "now()", strategy = FieldStrategy.NOT_NULL, fill = FieldFill.UPDATE)
    protected Date lastModifiedTime;

    @TableField("route_id")
    private String routeId;

    /**
     * 服务id
     */

    @TableField("service_id")
    private String serviceId;
    /**
     * 服务名称
     */
    @TableField("service_name")
    private String serviceName;
    /**
     * 是否有效（0无效，1有效）
     */
    @TableField("status")
    private Boolean status;
    /**
     * uri
     */
    @TableField("uri")
    private String uri;
    /**
     * 匹配路由优先级
     */
    @TableField("order")
    private Integer order;

    /**
     * ${name}=${args[0]},${args[1]}...${args[n]}
     * 多个用 - 分隔
     * ps: RouteDefinition
     * ${id}=${uri},${predicates[0]},${predicates[1]}...${predicates[n]}
     * *  eq:route001=http://127.0.0.1,Host=**.addrequestparameter.org,Path=/get
     * *  ps:单个 PredicateDefinition 的 args[i] 存在逗号( , ) ，会被错误的分隔，例如说，"Query=foo,bz"
     */
    @TableField("predicates")
    private String predicates;
    /**
     * 多个用 - 分隔
     * ${name}=${args[0]},${args[1]}...${args[n]}
     */
    @TableField("filters")
    private String filters;


    /**
     * 操作人
     */
    @TableField("operator")
    private String operator;
    public static final String PATH = "Path=/";
    public static final String SERVICE_ID = "service_id";
    public static final String ROUTE_ID = "route_id";
    public static final String SERVICE_NAME = "service_name";
    public static final String STATUS = "status";
    public static final String URI = "uri";
    public static final String ORDER = "order";
    public static final String PREDICATES = "predicates";
    public static final String OPERATOR = "operator";

}
