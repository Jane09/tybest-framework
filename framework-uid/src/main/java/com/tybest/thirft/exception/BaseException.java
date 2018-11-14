package com.tybest.thirft.exception;

/**
 * @author tb
 * @date 2018/11/14 15:08
 */
public class BaseException extends RuntimeException {


    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }
}
