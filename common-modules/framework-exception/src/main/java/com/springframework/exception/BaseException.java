package com.springframework.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author summer
 * 2018/8/13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseException extends RuntimeException{

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
}
