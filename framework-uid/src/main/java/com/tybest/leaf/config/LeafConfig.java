package com.tybest.leaf.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author tb
 * @date 2018/11/21 16:32
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "leaf")
public class LeafConfig {

    private int port;

    private int minWorkerThreads;

    private int maxWorkerThreads;

    private ZookeeperConfig zk;
}
