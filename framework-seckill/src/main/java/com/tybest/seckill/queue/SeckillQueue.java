package com.tybest.seckill.queue;

import com.tybest.seckill.entity.SuccessKilled;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author tb
 * @date 2018/12/4 11:42
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SeckillQueue {
    //队列大小
    private static final int QUEUE_MAX_SIZE   = 100;
    private BlockingQueue<SuccessKilled> blockingQueue = new LinkedBlockingQueue<>(QUEUE_MAX_SIZE);

    public static SeckillQueue getInstance(){
        return Holder.queue;
    }


    public boolean produce(SuccessKilled successKilled){
        return blockingQueue.offer(successKilled);
    }

    public SuccessKilled consume() throws InterruptedException {
        return blockingQueue.take();
    }

    public int size() {
        return blockingQueue.size();
    }

    private static class Holder{
        private static SeckillQueue queue = new SeckillQueue();
    }
}
