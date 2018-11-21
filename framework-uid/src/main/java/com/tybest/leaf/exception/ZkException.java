package com.tybest.leaf.exception;

/**
 * @author tb
 * @date 2018/11/14 15:09
 */
public class ZkException extends LeafException {

    public ZkException(String message) {
        super(message);
    }

    public ZkException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZkException(Throwable cause) {
        super(cause);
    }
}
