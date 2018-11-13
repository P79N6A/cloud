package com.springframework.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author summer
 */
@Slf4j
public class HostUtils {


    private static String ip;

    private static String hostName;

    private static String componentName;

    static {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress();
            hostName = addr.getHostName();
        } catch (UnknownHostException ex) {
           log.warn("UnknownHostException:{}",ex);
        }
    }

    public static String getHostName() {
        return hostName;
    }

    public static String getIp() {
        return ip;
    }

    public static String getComponentName() {
        return componentName;
    }

    public static void setComponentName(String componentName) {
        HostUtils.componentName = componentName;
    }
}
