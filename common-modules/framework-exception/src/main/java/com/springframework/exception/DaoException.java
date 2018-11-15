package com.springframework.exception;

import lombok.AllArgsConstructor;

/**
 * @author summer
 * 2018/11/15
 */
@AllArgsConstructor
public class DaoException extends BaseException {

    public DaoException( String internalErrorMessage){
        super();
        super.internalErrorMessage = internalErrorMessage;
    }

    @Override
    public BaseException get() {
        return this;
    }
}
