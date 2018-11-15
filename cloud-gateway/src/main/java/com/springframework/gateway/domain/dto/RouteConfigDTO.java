package com.springframework.gateway.domain.dto;

import com.google.common.collect.Lists;
import com.springframework.utils.JsonUtils;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;

import java.util.Date;
import java.util.List;

/**
 * @author summer
 * 2018/7/9
 */
@Data
@Slf4j
@ToString
public class RouteConfigDTO {
    private List<PredicateDefinition> predicateList = Lists.newArrayList();
    private List<FilterDefinition> filterList = Lists.newArrayList();
    private Long id;

    private String routeId;

    /**
     * @return 获取路由 PredicateDefinition{name='Path', args={pattern=/demo-server/**}}
     */
    public List<PredicateDefinition> getPredicateList() {
        if (StringUtils.isNotBlank(getPredicates())) {
            log.debug("路由参数{}", getPredicates());
            return JsonUtils.readJsonToObjectList(PredicateDefinition.class, getPredicates());
        }
        return predicateList;
    }

    /**
     * @return 过滤器
     */
    public List<FilterDefinition> getFilterList() {
        if (StringUtils.isNotBlank(getFilters())) {
            log.debug("路由过滤器参数{}", getFilters());
            return JsonUtils.readJsonToObjectList(FilterDefinition.class, getFilters());
        }
        return filterList;
    }

    /**
     * 服务id
     */

    private String serviceId;
    /**
     * 服务名称
     */
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
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 操作人
     */
    private String operator;

}
