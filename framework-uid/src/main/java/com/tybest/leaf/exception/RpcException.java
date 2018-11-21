package com.tybest.leaf.exception;

/**
 * @author tb
 * @date 2018/11/14 15:09
 */
public class RpcException extends LeafException {

    public RpcException(String message) {
        super(message);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }
}
