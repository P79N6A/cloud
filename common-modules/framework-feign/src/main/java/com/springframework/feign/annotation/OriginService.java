package com.springframework.feign.annotation;

import java.lang.annotation.*;

/**
 * 通过@OriginService注解的name属性配置了可以允许访问的服务名，如上配置，/testservice/exception3接口只允许product-center和wms-mst-center访问。
 * 没有使用@OriginService注解的接口将没有访问限制，所有服务都可以调用。
 *
 * @author summer  feign
 * 2018/7/31
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Documented
public @interface OriginService {
    String[] name() default "";
}
