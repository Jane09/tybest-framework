package com.tybest.seckill.queue.kafka;

import com.tybest.seckill.config.WebSocketServer;
import com.tybest.seckill.queue.Consumer;
import com.tybest.seckill.queue.redis.RedisOperator;
import com.tybest.seckill.service.SeckillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author tb
 * @date 2018/12/4 12:28
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer implements Consumer {

    private final SeckillService seckillService;
    private final RedisOperator redisOperator;
    private final WebSocketServer webSocketServer;


    @KafkaListener(topics = RedisOperator.CHANNEL)
    public void receiveMessage(String message) {
        process(redisOperator,seckillService,webSocketServer,message);
    }
}
