package com.tybest.seckill.queue.disruptor;


import com.lmax.disruptor.EventHandler;
import com.tybest.seckill.service.SeckillService;
import com.tybest.seckill.utils.SpringUtils;

/**
 * @author tb
 * @date 2018/12/4 12:04
 */
public class SeckillEventConsumer implements EventHandler<SeckillEvent> {

    private SeckillService seckillService = SpringUtils.getBean(SeckillService.class);

    @Override
    public void onEvent(SeckillEvent event, long l, boolean b) {
        seckillService.seckillSeq(event.getSeckillId(),event.getUserId());
    }
}
