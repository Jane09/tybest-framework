package com.tybest.seckill.controller;

import com.tybest.seckill.model.Result;
import com.tybest.seckill.queue.redis.RedisConsumer;
import com.tybest.seckill.queue.redis.RedisOperator;
import com.tybest.seckill.queue.redis.RedisProducer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tb
 * @date 2018/12/5 10:02
 */
@Api(tags ="分布式秒杀")
@RestController
@RequestMapping("/seckillDistributed")
@Slf4j
@RequiredArgsConstructor
public class SeckillDistributedController extends AbstractBaseController {

    private final RedisProducer redisProducer;
    private final RedisOperator redisOperator;

    @ApiOperation(value="秒杀一(Redisson分布式锁)-超卖")
    @PostMapping("/startRedissonLock")
    public Result startRedisLock(long seckillId){
        return seckill(seckillId, (seckillService, seckillId1, userId) -> {
            Result result = seckillService.seckilRedissonLock(seckillId1, userId);
            if(result!=null){
                log.info("用户:{}{}",userId,result.get("msg"));
            }else{
                log.info("用户:{}{}",userId,"哎呦喂，人也太多了，请稍后！");
            }
        });
    }

    @ApiOperation(value="秒杀二(Zookeeper分布式锁)-超卖")
    @PostMapping("/startZkLock")
    public Result startZkLock(long seckillId){
        return seckill(seckillId, (seckillService, seckillId1, userId) -> {
            Result result = seckillService.seckilZkLock(seckillId1, userId);
            if(result!=null){
                log.info("用户:{}{}",userId,result.get("msg"));
            }else{
                log.info("用户:{}{}",userId,"哎呦喂，人也太多了，请稍后！");
            }
        });
    }


    @ApiOperation(value="秒杀三(zk队列)")
    @PostMapping("/startRedisQueue")
    public Result startRedisQueue(long seckillId){
        return seckill(seckillId, (seckillService, seckillId1, userId) -> {
            final String key = ""+seckillId1;
            if(redisOperator.get(key) == null) {
                redisProducer.sendMessage(RedisOperator.CHANNEL,seckillId1+";"+userId);
            }
        });
    }
}
