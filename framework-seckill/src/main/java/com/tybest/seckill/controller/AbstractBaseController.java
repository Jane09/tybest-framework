package com.tybest.seckill.controller;

import com.tybest.seckill.model.Result;
import com.tybest.seckill.model.StateEnum;
import com.tybest.seckill.service.SeckillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author tb
 * @date 2018/12/4 11:15
 */
@Slf4j
public abstract class AbstractBaseController {

    private static int corePoolSize = Runtime.getRuntime().availableProcessors();
    private static ThreadPoolExecutor executor  = new ThreadPoolExecutor(corePoolSize, corePoolSize+1, 10L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            new BasicThreadFactory.Builder().namingPattern("seckill-task-%d").daemon(true).build(),
            new ThreadPoolExecutor.AbortPolicy());

    @Autowired
    protected SeckillService seckillService;
    @Value("${seckill.number}")
    private int seckillNum;

    protected Result seckill(long seckillId, SeckillProcesosr procesosr) {
        final CountDownLatch latch = new CountDownLatch(seckillNum);
        seckillService.reset(seckillId);
        final long id = seckillId;
        log.info("开始秒杀");
        for(int i=1;i<=seckillNum;i++){
            final long userId = i;
            executor.execute(() -> {
                procesosr.processor(seckillService,id,userId);
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


    protected Result seckillQueue(long seckillId, SeckillProcesosr procesosr) {
        seckillService.reset(seckillId);
        final long id = seckillId;
        log.info("开始秒杀");
        for(int i=1;i<=seckillNum;i++){
            final long userId = i;
            executor.execute(() -> {
                procesosr.processor(seckillService,id,userId);
            });
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            return Result.error(StateEnum.INNER_ERROR);
        }
        long count = seckillService.getSeckillCount(seckillId);
        log.info("共卖出 {} 件商品",count);
        return Result.ok();
    }
}
