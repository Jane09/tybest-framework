package com.tybest.crawler.exception;

/**
 * @author tb
 * @date 2018/11/27 15:52
 */
public class SpiderException extends RuntimeException {

    public SpiderException(String message) {
        super(message);
    }

    public SpiderException(Throwable cause) {
        super(cause);
    }
}
