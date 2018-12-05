package com.tybest.seckill.config;

import com.tybest.seckill.lock.zk.ZkLock;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tb
 * @date 2018/12/5 10:48
 */
@Configuration
@EnableConfigurationProperties(ZkProperty.class)
@RequiredArgsConstructor
public class ZkConfig {

    private final ZkProperty zkProperty;


    @Bean
    public ZkLock zkLock() {
        ZkLock zkLock = new ZkLock();
        zkLock.start(zkProperty.getHost(),zkProperty.getPort());
        return zkLock;
    }
}
