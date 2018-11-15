package com.springframework.eureka.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author summer
 * 2018/11/15
 */
@RestController
@RequestMapping("/service")
@Api
public class ServiceInstanceController {

    private DiscoveryClient discoveryClient;

    @Autowired
    public ServiceInstanceController(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @GetMapping("/serviceIds")
    @ApiOperation("当前有效服务注册ID列表")
    public ResponseEntity<List<String>> serviceIds() {
        return ResponseEntity.ok(discoveryClient.getServices());
    }

    /**
     * 服务注册实例
     *
     * @return
     */
    @GetMapping("/instance")
    @ApiOperation("所有服务实例列表")
    public ResponseEntity<List<ServiceInstance>> serviceInstance() {
        final List<String> serviceIds = discoveryClient.getServices();
        List<ServiceInstance> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(serviceIds)) {
            List<ServiceInstance> finalList = new ArrayList<>();
            serviceIds.parallelStream().forEach(serviceId -> {
                final List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
                finalList.addAll(instances);
            });
            list.addAll(finalList);
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/instance/{serviceId}")
    @ApiOperation("根据服务注册id查询实例列表")
    public ResponseEntity<List<ServiceInstance>> serviceInstanceById(@PathVariable("serviceId") String serviceId) {
        return ResponseEntity.ok(discoveryClient.getInstances(serviceId));
    }

}
