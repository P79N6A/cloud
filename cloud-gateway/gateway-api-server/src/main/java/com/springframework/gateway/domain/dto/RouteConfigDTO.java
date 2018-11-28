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
    /**
     * 谓词信息
     */
    private List<PredicateDefinition> predicateList;
    /**
     * 过滤器信息
     */
    private List<FilterDefinition> filterList;
    private Long id;

    private String routeId;

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
    private Integer status;
    /**
     * uri
     */
    private String uri;
    /**
     * 匹配路由优先级
     */
    private Integer orders;

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


    private Integer isDeleted;

    private String createdBy;
    private Date createdTime;
    private String lastModifiedBy;
    private Date lastModifiedTime;
    /**
     * 操作人
     */
    private String operator;

}
