package com.springframework.feign.configure;

import com.springframework.feign.annotation.OriginConfigProperties;
import com.springframework.feign.annotation.OriginService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;


/**
 * @author summer
 */
@Configuration
public class OriginServiceAutoConfiguration implements ImportBeanDefinitionRegistrar, InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        initConfig(importingClassMetadata);
    }

    private void initConfig(AnnotationMetadata importingClassMetadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata
                .getAnnotationAttributes(OriginService.class.getName()));
        if (null == attributes) {
            OriginConfigProperties.getOriginProperties().clear();
            return;
        }
        String[] comsumes = attributes.getStringArray("names");
        if (comsumes.length == 0) {
            return;
        }
        for (String comsume : comsumes) {
            OriginConfigProperties.getOriginProperties().add(comsume);
        }
    }
}
