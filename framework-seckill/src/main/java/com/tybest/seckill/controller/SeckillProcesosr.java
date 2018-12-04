package com.tybest.seckill.controller;

import com.tybest.seckill.service.SeckillService;

/**
 * @author tb
 */
@FunctionalInterface
public interface SeckillProcesosr {

    /**
     * 秒杀逻辑
     * @param seckillService    秒杀具体业务逻辑
     * @param seckillId         秒杀ID
     * @param userId            用户ID
     */
    void processor(SeckillService seckillService,long seckillId,long userId);
}
