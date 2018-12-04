package com.tybest.seckill.queue.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * @author tb
 * @date 2018/12/4 11:57
 */
public class SeckillEventFactory implements EventFactory<SeckillEvent> {

    @Override
    public SeckillEvent newInstance() {
        return new SeckillEvent();
    }
}
