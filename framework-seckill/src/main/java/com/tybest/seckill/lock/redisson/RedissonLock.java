package com.tybest.seckill.lock.redisson;

import lombok.Getter;
import org.redisson.api.RedissonClient;

/**
 * @author tb
 * @date 2018/12/5 10:25
 */
public class RedissonLock {
    @Getter
    private static RedissonClient redissonClient;

    public void setRedissonClient(RedissonClient redissonClient){
        RedissonLock.redissonClient = redissonClient;
    }
}
