package com.tybest.seckill.controller;

import com.tybest.seckill.model.Result;
import com.tybest.seckill.model.StateEnum;
import com.tybest.seckill.service.SeckillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author tb
 * @date 2018/12/3 13:49
 */
@Api(tags ="秒杀")
@RestController
@RequestMapping("/seckill")
@Slf4j
@RequiredArgsConstructor
public class SeckillController {

    private static int corePoolSize = Runtime.getRuntime().availableProcessors();
    private static ThreadPoolExecutor executor  = new ThreadPoolExecutor(corePoolSize, corePoolSize+1, 10L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            new BasicThreadFactory.Builder().namingPattern("seckill-task-%d").daemon(true).build(),
            new ThreadPoolExecutor.AbortPolicy());

    private final SeckillService seckillService;

    @Value("${seckill.number}")
    private int seckillNum;

    @ApiOperation(value="秒杀一(最low实现)")
    @PostMapping("/start")
    public Result start(long seckillId) {
        final CountDownLatch latch = new CountDownLatch(seckillNum);
        seckillService.reset(seckillId);
        final long id = seckillId;
        log.info("开始秒杀一，会出现超卖");
        for(int i=1;i<=seckillNum;i++){
            final long userId = i;
            executor.execute(() -> {
                Result result = seckillService.seckillOne(id,userId);
                if(null != result){
                    log.info("用户:{}{}", userId, result.get("msg"));
                }else {
                    log.info("用户:{}{}", userId, StateEnum.MUCH.getInfo());
                }
                latch.countDown();
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            log.error("线程{}被中断",Thread.currentThread().getName());
            return Result.error();
        }
        long count = seckillService.getSeckillCount(seckillId);
        log.info("共卖出 {} 件商品",count);
        return Result.ok();
    }

    @ApiOperation(value="秒杀二(程序锁)")
    @PostMapping("/startLock")
    public Result startLock(long seckillId){
        final CountDownLatch latch = new CountDownLatch(seckillNum);
        seckillService.reset(seckillId);
        final long id = seckillId;
        log.info("开始秒杀二，正常");
        for(int i=1;i<=seckillNum;i++){
            final long userId = i;
            executor.execute(() -> {
                Result result = seckillService.seckillLock(id,userId);
                log.info("用户:{}{}", userId, result.get("msg"));
                latch.countDown();
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            log.error("线程{}被中断",Thread.currentThread().getName());
            return Result.error();
        }
        long count = seckillService.getSeckillCount(seckillId);
        log.info("共卖出 {} 件商品",count);
        return Result.ok();
    }

}
