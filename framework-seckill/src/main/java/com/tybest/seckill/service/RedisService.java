package com.tybest.seckill.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * @author tb
 * @date 2018/12/3 11:11
 */
@Component
@Slf4j
public class RedisService {

    @Resource
    private RedisTemplate<Serializable, Serializable> redisTemplate;



}
