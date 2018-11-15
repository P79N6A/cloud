package com.springframework.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.function.Supplier;

/**
 * @author summer
 * 2018/8/13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class BaseException extends RuntimeException implements Supplier<BaseException> {

    /**
     * httpcode状态码
     */
    protected int httpCode;
    /**
     * 内部自定义code
     */
    protected int internalErrorCode;
    /**
     * 错误代码
     */
    protected int errorCode;
    /**
     * 错误信息
     */
    protected String errorMessage;
    /**
     * 内部错误信息
     */
    protected String internalErrorMessage;
    /**
     * 自定义信息
     */
    protected String customInfo;

    @Override
    public abstract BaseException get() ;
}
