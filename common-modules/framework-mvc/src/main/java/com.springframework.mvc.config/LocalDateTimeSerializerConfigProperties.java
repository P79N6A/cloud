package com.springframework.mvc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author summer
 * 2018/12/18
 */
@Component
@ConfigurationProperties(prefix = "spring.jackson.date-format")
@Data
public class LocalDateTimeSerializerConfigProperties {
    /**
     * pattern  yyyy-MM-dd HH:mm:ss
     */
    private String pattern = "yyyy-MM-dd HH:mm:ss";
}
