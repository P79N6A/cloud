package com.springframework.demo.domain.routeconfig.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author summer
 * 2018/7/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("route_config")
public class RouteConfig extends Model<RouteConfig>  {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
    private Boolean status;
    /**
     * uri
     */
    private String uri;
    /**
     * 匹配路由优先级
     */
    private Integer order;

    /**
     * ${name}=${args[0]},${args[1]}...${args[n]}
     * 多个用 - 分隔
     * ps: RouteDefinition
     * ${id}=${uri},${predicates[0]},${predicates[1]}...${predicates[n]}
     * *  eq:route001=http://127.0.0.1,Host=**.addrequestparameter.org,Path=/get
     * *  ps:单个 PredicateDefinition 的 args[i] 存在逗号( , ) ，会被错误的分隔，例如说，"Query=foo,bz"
     */
    private String predicates;
    /**
     * 多个用 - 分隔
     * ${name}=${args[0]},${args[1]}...${args[n]}
     */
    private String filters;

    /**
     * 获取路由规则
     *
     * @return 默认如果规则为空，按照serviceId去匹配
     */
//    public String getPredicates() {
//        return StringUtils.isBlank(predicates) ? PATH + this.getServiceId() : predicates;
//    }

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 操作人
     */
    private String operator;


    public static final String ID = "id";
    public static final String PATH = "Path=/";

    public static final String SERVICE_ID = "service_id";

    public static final String SERVICE_NAME = "service_name";

    public static final String STATUS = "status";

    public static final String URI = "uri";

    public static final String ORDER = "order";

    public static final String PREDICATES = "predicates";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String OPERATOR = "operator";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

