package com.tybest.seckill.controller;

import com.tybest.seckill.service.SeckillService;

@FunctionalInterface
public interface SeckillProcesosr {

    void processor(SeckillService seckillService,long seckillId,long userId);
}
