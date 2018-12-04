package com.tybest.seckill.controller;

import com.tybest.seckill.entity.SuccessKilled;
import com.tybest.seckill.model.Result;
import com.tybest.seckill.queue.SeckillQueue;
import com.tybest.seckill.queue.disruptor.DisruptorQueue;
import com.tybest.seckill.queue.disruptor.SeckillEvent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author tb
 * @date 2018/12/3 13:49
 */
@Api(tags ="秒杀")
@RestController
@RequestMapping("/seckill")
@Slf4j
public class SeckillController extends AbstractBaseController {

    @ApiOperation(value="秒杀一(最low实现)")
    @PostMapping("/start")
    public Result start(long seckillId) {
        return seckill(seckillId, (seckillService, seckillId1, userId) -> {
            Result result = seckillService.seckillOne(seckillId1, userId);
            if(result!=null){
                log.info("用户:{}{}",userId,result.get("msg"));
            }else{
                log.info("用户:{}{}",userId,"哎呦喂，人也太多了，请稍后！");
            }
        });
    }

    @ApiOperation(value="秒杀二(程序锁)")
    @PostMapping("/startLock")
    public Result startLock(long seckillId){
        return seckill(seckillId, (seckillService, seckillId1, userId) -> {
            Result result = seckillService.seckillLock(seckillId1, userId);
            if(result!=null){
                log.info("用户:{}{}",userId,result.get("msg"));
            }else{
                log.info("用户:{}{}",userId,"哎呦喂，人也太多了，请稍后！");
            }
        });
    }


    @ApiOperation(value="秒杀三(AOP程序锁)")
    @PostMapping("/startAopLock")
    public Result startAopLock(long seckillId){
        return seckill(seckillId, (seckillService, seckillId1, userId) -> {
            Result result = seckillService.seckillAopLock(seckillId1, userId);
            if(result!=null){
                log.info("用户:{}{}",userId,result.get("msg"));
            }else{
                log.info("用户:{}{}",userId,"哎呦喂，人也太多了，请稍后！");
            }
        });
    }


    @ApiOperation(value="秒杀四(数据库悲观锁)")
    @PostMapping("/startAopLock")
    public Result startPessLock(long seckillId){
        return seckill(seckillId, (seckillService, seckillId1, userId) -> {
            Result result = seckillService.seckillDbOne(seckillId1, userId);
            if(result!=null){
                log.info("用户:{}{}",userId,result.get("msg"));
            }else{
                log.info("用户:{}{}",userId,"哎呦喂，人也太多了，请稍后！");
            }
        });
    }

    @ApiOperation(value="秒杀五(数据库悲观锁)")
    @PostMapping("/startPessLock2")
    public Result startPessLock2(long seckillId){
        return seckill(seckillId, (seckillService, seckillId1, userId) -> {
            Result result = seckillService.seckillDbTwo(seckillId1, userId);
            if(result!=null){
                log.info("用户:{}{}",userId,result.get("msg"));
            }else{
                log.info("用户:{}{}",userId,"哎呦喂，人也太多了，请稍后！");
            }
        });
    }

    @ApiOperation(value="秒杀六(数据库乐观锁)")
    @PostMapping("/startOptLock")
    public Result startOptLock(long seckillId){
        return seckill(seckillId, (seckillService, seckillId1, userId) -> {
            Result result = seckillService.seckillOptLock(seckillId1, userId,1);
            if(result!=null){
                log.info("用户:{}{}",userId,result.get("msg"));
            }else{
                log.info("用户:{}{}",userId,"哎呦喂，人也太多了，请稍后！");
            }
        });
    }


    @ApiOperation(value="秒杀七(Redis/Kafka/MQ队列)")
    @PostMapping("/startQueue")
    public Result startQueue(long seckillId){
        return seckillQueue(seckillId, (seckillService, seckillId1, userId) -> {
            SuccessKilled kill = new SuccessKilled();
            kill.setSeckillId(seckillId1);
            kill.setUserId(userId);
            if(SeckillQueue.getInstance().produce(kill)){
                log.info("用户:{}{}",kill.getUserId(),"秒杀成功");
            }else {
                log.info("用户:{}{}",kill.getUserId(),"秒杀失败");
            }
        });
    }


    @ApiOperation(value="秒杀八(Disruptor)")
    @PostMapping("/startDisruptor")
    public Result startDisruptor(long seckillId){
        return seckillQueue(seckillId, (seckillService, seckillId1, userId) -> {
            SeckillEvent event = new SeckillEvent();
            event.setSeckillId(seckillId1);
            event.setUserId(userId);
            DisruptorQueue.produce(event);
        });
    }
}
