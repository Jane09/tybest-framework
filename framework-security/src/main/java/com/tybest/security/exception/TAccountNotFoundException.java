package com.tybest.security.exception;

/**
 * @author tb
 * @date 2018/11/15 15:48
 */
public class TAccountNotFoundException extends BaseException {


    public TAccountNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public TAccountNotFoundException(String msg) {
        super(msg);
    }
}
