package com.tybest.seckill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author tb
 * @date 2018/12/3 10:04
 */
@SpringBootApplication
public class SeckillApplication {

    /**
     * 1. 数据库乐观锁；
     * 2. 基于Redis的分布式锁；
     * 3. 基于ZooKeeper的分布式锁
     * 4. redis 订阅监听；
     * 5. kafka消息队列
     * 启动前 请配置application.properties中相关redis、zk以及kafka相关地址
     */
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(SeckillApplication.class);
    }
}
