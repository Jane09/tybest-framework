package com.tybest.seckill.queue.redis;

import com.tybest.seckill.config.WebSocketServer;
import com.tybest.seckill.queue.Consumer;
import com.tybest.seckill.service.SeckillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author tb
 * @date 2018/12/4 12:28
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisConsumer implements Consumer {

    private final SeckillService seckillService;
    private final WebSocketServer webSocketServer;
    private final RedisOperator redisOperator;


    public void receiveMessage(String message) {
        log.info("received message: {}", message);
        process(redisOperator,seckillService,webSocketServer,message);
    }

}
