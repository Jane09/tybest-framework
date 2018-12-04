package com.tybest.seckill.exception;

/**
 *
 * @author tb
 * @date 2018/12/4 9:43
 */
public class SeckillException extends RuntimeException {

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }

    public SeckillException(Throwable cause) {
        super(cause);
    }
}
