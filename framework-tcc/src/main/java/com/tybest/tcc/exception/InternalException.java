package com.tybest.tcc.exception;

/**
 * @author tb
 * @date 2018/12/10 17:36
 */
public class InternalException extends AbstractTccException {
    public InternalException(String message) {
        super(message);
    }

    public InternalException(Throwable cause) {
        super(cause);
    }

    public InternalException(String message, Throwable cause) {
        super(message, cause);
    }
}
