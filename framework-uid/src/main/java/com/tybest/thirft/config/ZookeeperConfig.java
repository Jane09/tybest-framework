package com.tybest.thirft.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author tb
 * @date 2018/11/14 12:07
 */
@Configuration
@ConfigurationProperties(prefix = "leaf.zookeeper")
@Getter
@Setter
public class ZookeeperConfig {

    private int port;
    private List<String> servers;
    private String root;
    private String forever;
    private String ephemeral;
    private int sessionTimeoutMs;
    private int connectionTimeoutMs;
    private int retryTimes;
    private int retryIntervalMs;
    private int retryIntervalceilingMs;
    private int heartbeatIntervalMs;
}
