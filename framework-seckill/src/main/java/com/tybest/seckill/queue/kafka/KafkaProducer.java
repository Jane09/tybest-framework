package com.tybest.seckill.queue.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author tb
 * @date 2018/12/4 12:28
 */
@Component
public class KafkaProducer {

    @Resource
    private KafkaTemplate<String,String> kafkaTemplate;


    public void sendMessage(String channel, String message) {
        kafkaTemplate.send(channel,message);
    }
}
