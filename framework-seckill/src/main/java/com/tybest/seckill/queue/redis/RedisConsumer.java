package com.tybest.seckill.queue.redis;

import com.tybest.seckill.service.SeckillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author tb
 * @date 2018/12/4 12:28
 */
@Component
@RequiredArgsConstructor
public class RedisConsumer {

    private final SeckillService seckillService;

}
