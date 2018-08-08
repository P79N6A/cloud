package com.springframework.log.annotation;

import java.util.HashSet;
import java.util.Set;

public class LogConfigProperties {

    private static final Set<String> logProperties = new HashSet<>();

    public static final Set<String> getLogProperties() {
        return logProperties;
    }

    public static String format(String interfaceName, String methodName) {
        return interfaceName + "/" + methodName;
    }
}
