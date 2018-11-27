package com.tybest.crawler.utils;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @author tb
 * @date 2018/11/27 12:05
 */
public final class Utils {

    public static void sleep(long time){
        try {
            TimeUnit.MILLISECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static <E> boolean isEmpty(Collection<E> collection){
        return null == collection || collection.size() == 0;
    }
}
