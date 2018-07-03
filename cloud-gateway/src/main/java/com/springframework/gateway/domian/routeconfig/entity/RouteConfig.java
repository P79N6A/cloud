package com.springframework.gateway.domian.routeconfig.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.google.common.collect.Lists;
import lombok.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author summer
 * 2018/7/2
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("route_config")
public class RouteConfig implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<PredicateDefinition> predicateList = Lists.newArrayList();
    private List<FilterDefinition> filterList = Lists.newArrayList();
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * @return 获取路由
     */
    public List<PredicateDefinition> getPredicateList() {
        if (StringUtils.isNotBlank(getPredicates())) {
            final String[] predicateArr = StringUtils.split(getPredicates(), ",");
            Arrays.stream(predicateArr).forEach(s -> {
                PredicateDefinition predicateDefinition = new PredicateDefinition(s);
                predicateList.add(predicateDefinition);
            });
        }
        return predicateList;
    }

    /**
     * @return 过滤器
     */
    public List<FilterDefinition> getFilterList() {
        if (StringUtils.isNotBlank(getFilters())) {
            final String[] filterArr = StringUtils.split(getFilters(), ",");
            Arrays.stream(filterArr).forEach(s -> {
                FilterDefinition filterDefinition = new FilterDefinition(s);
                filterList.add(filterDefinition);
            });
        }
        return filterList;
    }

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
     * 路由匹配法则，多个用逗号隔开（ps:如果为空，默认以service_id匹配）
     */
    private String predicates;
    /**
     * 多个用逗号隔开
     */
    private String filters;

    /**
     * 获取路由规则
     *
     * @return 默认如果规则为空，按照serviceId去匹配
     */
    public String getPredicates() {
        return StringUtils.isBlank(predicates) ? PATH + this.getServiceId() : predicates;
    }

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

}
