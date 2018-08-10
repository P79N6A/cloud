package com.springframework.zipkin.elk;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import zipkin.server.EnableZipkinServer;

/**
 * @author summer
 * 2018/8/10
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableZipkinServer
public class ZipkinElkApplication {
}
