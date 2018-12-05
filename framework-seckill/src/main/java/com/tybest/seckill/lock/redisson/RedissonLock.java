package com.tybest.seckill.lock.redisson;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @author tb
 * @date 2018/12/5 10:25
 */
@Slf4j
public class RedissonLock {
    @Getter
    private static RedissonClient redissonClient;

    public void setRedissonClient(RedissonClient redissonClient){
        RedissonLock.redissonClient = redissonClient;
    }

    public static void lock(String key) {
        redissonClient.getLock(key).lock();
    }

    public static void lock(String key, int timeout) {
        lock(key,TimeUnit.SECONDS,timeout);
    }

    public static void lock(String key, TimeUnit unit, int timeout) {
        redissonClient.getLock(key).lock(timeout,unit);
    }

    public static boolean tryLock(String key,int waitTime,int releaseTime) {
        return tryLock(key,TimeUnit.SECONDS,waitTime,releaseTime);
    }

    public static boolean tryLock(String key,TimeUnit unit,int waitTime,int releaseTime) {
        RLock rLock = redissonClient.getLock(key);
        try {
            return rLock.tryLock(waitTime,releaseTime,unit);
        } catch (InterruptedException e) {
            log.error("Try lock failed",e);
        }
        return false;
    }


    public static void unlock(String key) {
        RLock rLock = redissonClient.getLock(key);
        rLock.unlock();
    }
}
