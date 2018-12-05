package com.tybest.seckill.queue;

import com.tybest.seckill.config.WebSocketServer;
import com.tybest.seckill.model.Result;
import com.tybest.seckill.model.StateEnum;
import com.tybest.seckill.queue.redis.RedisOperator;
import com.tybest.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tb
 */
public interface Consumer {

    Logger log = LoggerFactory.getLogger(Consumer.class);
    default void process(RedisOperator redisOperator, SeckillService seckillService, WebSocketServer webSocketServer,String message) {
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
