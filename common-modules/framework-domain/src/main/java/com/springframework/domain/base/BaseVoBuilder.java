package com.springframework.domain.base;

/**
 * @author summer
 * 2018/8/13
 */
public abstract class BaseVoBuilder<T extends BaseVo> {

    public abstract T build();
}
