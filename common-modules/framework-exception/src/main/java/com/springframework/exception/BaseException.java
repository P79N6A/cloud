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
    private int httpCode;
    /**
     * 内部自定义code
     */
    private int internalErrorCode;
    /**
     * 错误代码
     */
    private int errorCode;
    /**
     * 错误信息
     */
    private String errorMessage;
    /**
     * 内部错误信息
     */
    private String internalErrorMessage;
    /**
     * 自定义信息
     */
    private String customInfo;

    @Override
    public abstract BaseException get() ;
}
