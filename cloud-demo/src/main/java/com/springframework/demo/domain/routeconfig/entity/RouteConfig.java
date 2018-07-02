package com.springframework.demo.domain.routeconfig.entity;

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
public class RouteConfig {

    private Long uId;
    private String serviecName;
    private String serviecId;
    private String uri;
    private Integer order;
    /**
     * 多个predicate,用逗号分隔
     */
    private String predicates;

}
