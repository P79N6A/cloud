package com.springframework.feign.annotation;

import java.util.HashSet;
import java.util.Set;

/**
 * @author summer
 */
public class OriginConfigProperties {

    private static volatile Set<String> originProperties = new HashSet<>();

    public static final Set<String> getOriginProperties() {
        return originProperties;
    }

}
