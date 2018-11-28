package com.tybest.jdbc.sharding;

import com.tybest.jdbc.sharding.raw.SpringBootDbSharding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.sql.SQLException;

/**
 * @author tb
 * @date 2018/11/28 10:15
 */
@SpringBootApplication
public class ShardingApplication {

    public static void main(String[] args) throws SQLException {
        ApplicationContext context = SpringApplication.run(ShardingApplication.class);
        SpringBootDbSharding sharding = context.getBean(SpringBootDbSharding.class);
        sharding.test();
    }
}
