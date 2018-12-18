package com.springframework.mvc.config;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author summer
 * 2018/12/18
 */
@Configuration
@EnableConfigurationProperties(LocalDateTimeSerializerConfigProperties.class)
public class LocalDateTimeSerializerAutoConfig {

    private final LocalDateTimeSerializerConfigProperties localDateTimeSerializerConfigProperties;

    @Autowired
    public LocalDateTimeSerializerAutoConfig(LocalDateTimeSerializerConfigProperties localDateTimeSerializerConfigProperties) {
        this.localDateTimeSerializerConfigProperties = localDateTimeSerializerConfigProperties;
    }

    @Bean
    public LocalDateTimeSerializer localDateTimeDeserializer() {
        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(localDateTimeSerializerConfigProperties.getPattern()));
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder.serializerByType(LocalDateTime.class, localDateTimeDeserializer());
    }
}
