package com.tybest.tcc.exception;

/**
 * @author tb
 * @date 2018/12/7 15:54
 */
public class CancelingException extends AbstractTccException {

    public CancelingException(String message) {
        super(message);
    }
    public CancelingException(Throwable cause) {
        super(cause);
    }
}
