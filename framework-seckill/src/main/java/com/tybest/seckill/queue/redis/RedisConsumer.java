package com.tybest.seckill.queue.redis;

import com.tybest.seckill.config.WebSocketServer;
import com.tybest.seckill.model.Result;
import com.tybest.seckill.model.StateEnum;
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
public class RedisConsumer {

    private final SeckillService seckillService;
    private final WebSocketServer webSocketServer;
    private final RedisOperator redisOperator;


    public void receiveMessage(String message) {
        log.info("received message: {}", message);
        String[] ms = message.split(";");
        final long seckillId = Long.parseLong(ms[0]);
        final long userId = Long.parseLong(ms[1]);
        if(redisOperator.get(seckillId+"") == null){
            Result result = seckillService.seckillDbTwo(seckillId,userId);
            try{
                if(result.equals(Result.ok(StateEnum.SUCCESS))) {
                    webSocketServer.sendInfo(StateEnum.SUCCESS.getInfo(),userId+"");
                    redisOperator.add(seckillId+"","ok");
                }else {
                    webSocketServer.sendInfo(StateEnum.FINISH.getInfo(),userId+"");
                }
            }catch (Exception ex) {
                log.error("send message to user failed",ex);
            }
        }else {
            webSocketServer.sendInfo(StateEnum.REPEAT.getInfo(),userId+"");
        }
    }

}
