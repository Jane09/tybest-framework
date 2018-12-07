package com.tybest.tcc.exception;

/**
 * @author tb
 * @date 2018/12/7 15:54
 */
public class TryingException extends AbstractTccException {

    public TryingException(String message) {
        super(message);
    }

    public TryingException(Throwable cause) {
        super(cause);
    }
}
