package com.tybest.seckill.controller;

import com.tybest.seckill.model.Result;
import com.tybest.seckill.service.SeckillService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    public Result start(long seckillId) {

        return Result.ok();
    }
}
