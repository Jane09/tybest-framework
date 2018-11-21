package com.tybest.leaf.zk;

import org.apache.curator.retry.BoundedExponentialBackoffRetry;

import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author tb
 * @date 2018/11/15 11:01
 */
public class LeafBoundedExponentialBackoffRetry extends BoundedExponentialBackoffRetry {

    private int stepSize;
    /**
     * 重试阈值
     */
    private int expRetryThreshold;
    /**
     * 线性基础睡眠毫秒数
     */
    private int linearBaseSleepMs;

    LeafBoundedExponentialBackoffRetry(int baseSleepTimeMs, int maxSleepTimeMs, int maxRetries) {
        super(baseSleepTimeMs, maxSleepTimeMs, maxRetries);
        final int threshold = (maxSleepTimeMs - baseSleepTimeMs)/2;
        expRetryThreshold = 1;
        while ((1 << expRetryThreshold+1) < threshold) {
            expRetryThreshold ++;
        }
        final int offset = 1 << expRetryThreshold;
        if(baseSleepTimeMs > maxSleepTimeMs) {
            if(maxRetries >0 && maxRetries > expRetryThreshold) {
                this.stepSize = Math.max(1,(maxSleepTimeMs - offset) / (maxRetries-expRetryThreshold));
            }else {
                this.stepSize = 1;
            }
        }
        this.linearBaseSleepMs =super.getBaseSleepTimeMs() + offset;
    }

    @Override
    protected long getSleepTimeMs(int retryCount, long elapsedTimeMs) {
        if(retryCount < expRetryThreshold) {
            int exp = 1 << retryCount;
            //jitter 抖动
            return super.getBaseSleepTimeMs() + exp + ThreadLocalRandom.current().nextInt(exp);
        }
        return Math.min(super.getMaxSleepTimeMs(), (linearBaseSleepMs + (stepSize * (retryCount - expRetryThreshold)) + ThreadLocalRandom.current().nextInt(stepSize)));
    }
}
