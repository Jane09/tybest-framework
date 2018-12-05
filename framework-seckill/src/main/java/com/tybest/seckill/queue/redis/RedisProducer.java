package com.tybest.seckill.queue.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author tb
 * @date 2018/12/4 12:28
 */
@Component
@RequiredArgsConstructor
public class RedisProducer {

    private final StringRedisTemplate stringRedisTemplate;



    public void sendMessage(String channel, String message) {
        stringRedisTemplate.convertAndSend(channel,message);
    }
}
