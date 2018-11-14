package com.tybest.thirft.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author tb
 * @date 2018/11/14 11:58
 */
@Configuration
@ConfigurationProperties(prefix = "leaf.rpc")
@Getter
@Setter
public class RpcConfig {


}
