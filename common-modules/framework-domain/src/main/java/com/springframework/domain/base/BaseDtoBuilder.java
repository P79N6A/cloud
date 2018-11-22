package com.springframework.domain.base;

/** @author summer 2018/8/13 */
public abstract class BaseDtoBuilder<T extends BaseDTO> {

  public abstract T build();
}
