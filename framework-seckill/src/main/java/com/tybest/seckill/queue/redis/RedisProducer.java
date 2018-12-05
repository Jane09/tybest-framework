package com.tybest.seckill.queue.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author tb
 * @date 2018/12/4 12:28
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisProducer {

    private final StringRedisTemplate stringRedisTemplate;



    public void sendMessage(String channel, String message) {
        log.info("sendMessage: {}", message);
        stringRedisTemplate.convertAndSend(channel,message);
    }
}
