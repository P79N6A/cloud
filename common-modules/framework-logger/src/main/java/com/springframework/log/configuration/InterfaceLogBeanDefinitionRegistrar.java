package com.springframework.log.configuration;

import com.springframework.log.annotation.EnableLogPersist;
import com.springframework.log.annotation.LogConfigProperties;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.reflect.Method;

/**
 * @author summer
 */
@Configuration
public class InterfaceLogBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        initLogConfig(importingClassMetadata);
        ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
        if (this.resourceLoader != null) {
            scanner.setResourceLoader(this.resourceLoader);
        }
        scanner.registerFilters();
        scanner.doScan("com.weimob.cube.log.dao");
    }

    private void initLogConfig(AnnotationMetadata importingClassMetadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata
                .getAnnotationAttributes(EnableLogPersist.class.getName()));
        AnnotationAttributes[] comsumes = attributes.getAnnotationArray("comsumes");
        if (comsumes == null || comsumes.length == 0) {
            return;
        }
        for (AnnotationAttributes comsume : comsumes) {
            Class<?> aClass = comsume.getClass("api");
            String[] method = comsume.getStringArray("method");

            if (method != null && "*".equals(method[0])) {
                Method[] methods = aClass.getMethods();
                for (int i = 0; i < methods.length; i++) {
                    String methodName = methods[i].getName();
                    LogConfigProperties.getLogProperties().add(LogConfigProperties.format(aClass.getName(), methodName));
                }
            } else {
                for (int i = 0; i < method.length; i++) {
                    LogConfigProperties.getLogProperties().add(LogConfigProperties.format(aClass.getName(), method[i]));
                }
            }

        }
    }


}
