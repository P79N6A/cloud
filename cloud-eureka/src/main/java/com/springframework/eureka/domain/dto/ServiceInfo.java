package com.springframework.eureka.domain.dto;

import java.util.Map;

/**
 * @author summer
 * 2018/11/15
 */
public class ServiceInfo {
    private String serviceId;
    private String host;
    private String port;
    private boolean secure;
    private Map<String, Object> metadata;
    private String uri;
}
