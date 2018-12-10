package com.tybest.tcc.exception;

/**
 * @author tb
 * @date 2018/12/7 15:53
 */
public abstract class AbstractTccException extends RuntimeException {

    public AbstractTccException(String message) {
        super(message);
    }

    public AbstractTccException(Throwable cause) {
        super(cause);
    }

    public AbstractTccException(String message, Throwable cause) {
        super(message, cause);
    }
}
