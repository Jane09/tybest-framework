package com.tybest.tcc.exception;

/**
 * @author tb
 * @date 2018/12/7 15:55
 */
public class ConfirmingException extends AbstractTccException {
    public ConfirmingException(String message) {
        super(message);
    }

    public ConfirmingException(Throwable cause) {
        super(cause);
    }
}
