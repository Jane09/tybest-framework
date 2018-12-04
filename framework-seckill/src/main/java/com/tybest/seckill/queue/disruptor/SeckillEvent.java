package com.tybest.seckill.queue.disruptor;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author tb
 * @date 2018/12/4 11:57
 */
@Getter
@Setter
public class SeckillEvent implements Serializable {
    private long seckillId;
    private long userId;
}
