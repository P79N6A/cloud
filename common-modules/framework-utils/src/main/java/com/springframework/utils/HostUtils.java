package com.springframework.utils;

import com.springframework.constants.Constant;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.com.springframework.common.exception.log.ExceptionLogObject;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * Created by kainlin on 2015/10/17.
 */
public class HostUtils {

    private static final Logger exLoger = LoggerFactory.getLogger(Constant.EXCPETION_LOGGER);

    private static String ip;

    private static String hostName;

    private static String componentName;

    static {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress();
            hostName = addr.getHostName();
        } catch (UnknownHostException ex) {
            new ExceptionLogObject(
                    Constant.LOGLEVEL_ERROR,
                    new Date(),
                    ex.getClass().getName(),
                    ExceptionUtils.getStackTrace(ex),
                    ex.getMessage(),
                    Constant.ERRORCODE_INTERNAL_INITFAIL,
                    String.format(Constant.ERRORMESSAGE_INTERNAL_INITFAIL, "HostUtils"),
                    "unknown",
                    "unknown",
                    "unknown",
                    null).log(exLoger);
            ip = "unknown";
            hostName = "unknown";
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
