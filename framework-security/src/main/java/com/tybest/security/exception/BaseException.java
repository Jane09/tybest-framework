package com.tybest.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author tb
 * @date 2018/11/15 15:48
 */
public abstract class BaseException extends AuthenticationException {


    public BaseException(String msg, Throwable t) {
        super(msg, t);
    }

    public BaseException(String msg) {
        super(msg);
    }
}
