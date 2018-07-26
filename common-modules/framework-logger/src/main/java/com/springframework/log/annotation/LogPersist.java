package com.springframework.log.annotation;


import java.lang.annotation.*;

/**
 * @EnableLogPersist(
 *         comsumes = {
 *                 @LogConsumeInterface(api=String.class,method="123")
 *         }
 * )
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface LogPersist {
}
