package com.tybest.leaf.config;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author tb
 * @date 2018/11/21 16:42
 */
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
    private int averageTimestampThreshold;
}
