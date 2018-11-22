package com.tybest.leaf.exception;

/**
 * @author tb
 * @date 2018/11/22 14:21
 */
public class InternalException extends LeafException {

    public InternalException(String message) {
        super(message);
    }

    public InternalException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalException(Throwable cause) {
        super(cause);
    }
}
