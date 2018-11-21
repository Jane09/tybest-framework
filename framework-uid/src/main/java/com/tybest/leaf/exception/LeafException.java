package com.tybest.leaf.exception;

/**
 * @author tb
 * @date 2018/11/14 15:08
 */
class LeafException extends RuntimeException {


    LeafException(String message) {
        super(message);
    }

    LeafException(String message, Throwable cause) {
        super(message, cause);
    }

    LeafException(Throwable cause) {
        super(cause);
    }
}
