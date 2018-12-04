package com.tybest.seckill.queue.disruptor;

import com.lmax.disruptor.EventTranslatorVararg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

/**
 * @author tb
 * @date 2018/12/4 11:56
 */
public final class DisruptorQueue {

    private static Disruptor<SeckillEvent> disruptor;
    private static final int RING_BUFFER_SIZE = 1024;
    private static final EventTranslatorVararg<SeckillEvent> TRANSLATOR_VARARG = (event, seq, objs) -> {
        event.setSeckillId((Long)objs[0]);
        event.setUserId((Long)objs[1]);
    };

    static {
        disruptor = new Disruptor<>(new SeckillEventFactory(), RING_BUFFER_SIZE,
                new BasicThreadFactory.Builder().namingPattern("disruptor-taaks-%d").build());
        disruptor.handleEventsWith(new SeckillEventConsumer());
        disruptor.start();
    }

    public static void produce(SeckillEvent event) {
        RingBuffer<SeckillEvent> ringBuffer = disruptor.getRingBuffer();
        ringBuffer.publishEvent(TRANSLATOR_VARARG,event.getSeckillId(),event.getUserId());
    }
}
