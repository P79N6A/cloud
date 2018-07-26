package com.springframework.log.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口日志
 */
@Data
public class InterfaceLog implements Serializable {

    private static final long serialVersionUID = 2841138837344518003L;

    private Long id;
    private String consumerApplication;
    private String consumerHost;
    private String providerApplication;
    private String providerHost;
    private String interfaceName;
    private String methodeName;
    private String requestParam;
    private String response;
}
