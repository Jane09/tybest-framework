package com.tybest.seckill.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author tb
 * @date 2018/12/5 10:47
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "zk")
public class ZkProperty {

    private String host;
    private int port;
}
