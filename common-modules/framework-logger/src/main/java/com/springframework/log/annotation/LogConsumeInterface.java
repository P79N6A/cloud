package com.springframework.log.annotation;

import java.lang.annotation.*;

/**
 * 消费者
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface LogConsumeInterface {
    Class<?> api();

    String[] method();
}
