package com.tybest.seckill.queue.disruptor;

import com.lmax.disruptor.EventTranslatorVararg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

/**
 * @author tb
 * @date 2018/12/4 11:56
 */
@Slf4j
public final class DisruptorQueue {

    private static Disruptor<SeckillEvent> disruptor;
    private static final int RING_BUFFER_SIZE = 1024;
    private static volatile boolean started = false;
    private static final EventTranslatorVararg<SeckillEvent> TRANSLATOR_VARARG = (event, seq, objs) -> {
        event.setSeckillId((Long)objs[0]);
        event.setUserId((Long)objs[1]);
    };


    public synchronized static void start(){
        if(started) {
            return;
        }
        disruptor = new Disruptor<>(new SeckillEventFactory(), RING_BUFFER_SIZE,
                new BasicThreadFactory.Builder().namingPattern("disruptor-taaks-%d").build());
        disruptor.handleEventsWith(new SeckillEventConsumer());
        disruptor.start();
        started = true;
        log.info("disruptor started");
    }

    public static void produce(SeckillEvent event) {
        if(!started) {
            start();
        }
        log.info("disruptor enqueue={}",event.getUserId());
        RingBuffer<SeckillEvent> ringBuffer = disruptor.getRingBuffer();
        ringBuffer.publishEvent(TRANSLATOR_VARARG,event.getSeckillId(),event.getUserId());
    }
}
