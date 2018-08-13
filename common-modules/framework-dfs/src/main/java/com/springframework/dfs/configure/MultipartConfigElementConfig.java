package com.springframework.dfs.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

/**
 * @author summer
 * 2018/8/13
 */
@Configuration
public class MultipartConfigElementConfig {
    @Value("${fdfs.max.request.size:10MB}")
    private String maxRequestSize;
    @Value("${fdfs.max.file.size:10MB}")
    private String maxFileSize;

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        //设置文件上传的上限
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(maxFileSize);
        factory.setMaxRequestSize(maxRequestSize);
        //factory.setLocation("/tmp");
        return factory.createMultipartConfig();
    }
}
