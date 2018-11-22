package com.tybest.leaf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author tb
 * @date 2018/11/21 16:13
 */
@SpringBootApplication
@EnableConfigurationProperties
public class LeafApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeafApplication.class);
    }
}
