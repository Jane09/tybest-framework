package com.tybest.seckill.queue.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author tb
 * @date 2018/12/5 11:43
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisOperator {

    private final RedisTemplate<Serializable, Serializable> redisTemplate;

    private static final String KEY_PREFIX_VALUE = "tybest:seckill:value:";


    public boolean add(String k, Serializable v) {
        return add(k,v,-1);
    }

    public boolean add(String k, Serializable v, long time) {
        return add(k,v,time,TimeUnit.SECONDS);
    }

    public boolean add(String k, Serializable v, long time,TimeUnit unit) {
        final String key = KEY_PREFIX_VALUE+k;
        try{
            ValueOperations<Serializable,Serializable> opts = redisTemplate.opsForValue();
            opts.set(key,v);
            if(time > 0){
                redisTemplate.expire(key,time, unit);
            }
            return true;
        }catch (Throwable r) {
            log.error("缓存[{}]失败, value[{}]",key,v,r);
        }

        return false;
    }


    public boolean contains(String k) {
        final String key = KEY_PREFIX_VALUE+k;
        try{
            Boolean b = redisTemplate.hasKey(key);
            return b ==null?false:b;
        }catch (Throwable r) {
            log.error("判断缓存失败key[" + key + ", error[" + r + "]");
        }
        return false;
    }


    public Serializable get(String k) {
        final String key = KEY_PREFIX_VALUE+k;
        try{
            ValueOperations<Serializable,Serializable> opts = redisTemplate.opsForValue();
            return opts.get(key);
        }catch (Throwable r) {
            log.error("获取缓存失败key[" + KEY_PREFIX_VALUE + k + ", error[" + r + "]");
        }
        return null;
    }

    public boolean delete(String k) {
        final String key = KEY_PREFIX_VALUE+k;
        try{
            Boolean d = redisTemplate.delete(key);
            return d==null?false:d;
        }catch (Throwable r) {
            log.error("删除缓存失败key[" + key + ", error[" + r + "]");
        }
        return false;
    }
}
