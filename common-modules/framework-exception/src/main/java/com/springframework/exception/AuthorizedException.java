package com.springframework.exception;

/**
 * @author summer 授权类异常
 * 2018/8/27
 */
public class AuthorizedException extends BaseKnownException{
    @Override
    public BaseException get() {
        return this;
    }
}
