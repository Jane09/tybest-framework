package com.tybest.thirft.zookeeper.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author tb
 * @date 2018/11/15 13:59
 */
@Slf4j
public final class ProcessUtils {

    private static final long ONE_MILLIS = 1000L;

    public static void exitDelay(int val, String message) {
        log.info("exit message: {}", message);
        try {
            Thread.sleep(ONE_MILLIS);
        } catch (InterruptedException e) {
            //do nothing
        }
        exit(val);
    }

    private static void exit(int val) {
        System.exit(val);
    }


    public static void sleep(long timeInMs) {
        try {
            Thread.sleep(timeInMs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
