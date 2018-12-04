package com.tybest.seckill.lock.redis;

/**
 * @author tb
 * @date 2018/12/4 12:31
 */
public class RedisLock {
    private static final String SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";


}
