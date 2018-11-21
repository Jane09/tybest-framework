package com.tybest.security.exception;

/**
 * @author tb
 * @date 2018/11/21 15:17
 */
public class IllegalRawTokenException extends BaseException {

    public IllegalRawTokenException(String msg, Throwable t) {
        super(msg, t);
    }

    public IllegalRawTokenException(String msg) {
        super(msg);
    }
}
