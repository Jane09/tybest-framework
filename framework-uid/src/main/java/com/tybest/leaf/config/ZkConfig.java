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
public class ZkConfig {

    private long datacenter;
    private long workerId;
    private int port;
    private List<String> servers;
    private String persistent;
    private String ephemeral;
    private int sessionTimeoutMs;
    private int connectionTimeoutMs;
    private int retryTimes;
    private int retryIntervalMs;
    private int retryIntervalceilingMs;
    private int heartbeatIntervalMs;
    private int averageTimestampThreshold;
}
