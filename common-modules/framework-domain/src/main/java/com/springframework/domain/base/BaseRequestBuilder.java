package com.springframework.domain.base;

/**
 * @author summer
 * 2018/8/13
 */
public abstract class BaseRequestBuilder<T extends BaseRequest> {
    public abstract T build();
}
