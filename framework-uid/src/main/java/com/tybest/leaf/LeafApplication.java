package com.tybest.leaf;

import com.tybest.leaf.zk.ZkServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author tb
 * @date 2018/11/21 16:13
 */
@SpringBootApplication
public class LeafApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(LeafApplication.class);
        ZkServer server = context.getBean(ZkServer.class);
        server.start(null);
    }
}
