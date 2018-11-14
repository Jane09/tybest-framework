package com.tybest.thirft.exception;

/**
 * @author tb
 * @date 2018/11/14 15:09
 */
public class ZookeeperException extends BaseException {

    public ZookeeperException(String message) {
        super(message);
    }

    public ZookeeperException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZookeeperException(Throwable cause) {
        super(cause);
    }
}
