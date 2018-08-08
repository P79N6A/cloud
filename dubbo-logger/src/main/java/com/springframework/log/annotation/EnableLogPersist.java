package com.springframework.log.annotation;

import com.springframework.log.aop.LogPersistAspect;
import com.springframework.log.configuration.InterfaceLogAutoConfiguration;
import com.springframework.log.configuration.InterfaceLogBeanDefinitionRegistrar;
import com.springframework.log.service.impl.InterfaceLogServiceImpl;
import com.springframework.log.task.DeleteInterfaceLogTask;
import com.springframework.log.util.ApplicationContextUtil;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(
        {
                InterfaceLogBeanDefinitionRegistrar.class,
                InterfaceLogAutoConfiguration.class,
                LogPersistAspect.class,
                InterfaceLogServiceImpl.class,
                ApplicationContextUtil.class,
                DeleteInterfaceLogTask.class
        })
public @interface EnableLogPersist {
    LogConsumeInterface[] comsumes() default {};
}
