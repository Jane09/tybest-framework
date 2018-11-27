package com.tybest.crawler.utils;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author tb
 * @date 2018/11/27 13:52
 */
@RequiredArgsConstructor
public class NamedThreadFactory implements ThreadFactory {

    private final String prefix;
    private final LongAdder threadNumber = new LongAdder();

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r,prefix+"@thread-"+threadNumber.intValue());
    }
}
