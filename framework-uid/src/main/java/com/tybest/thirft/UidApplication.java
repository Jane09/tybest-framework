package com.tybest.thirft;

import com.tybest.thirft.rpc.RpcUidServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
/**
 * @author tb
 * @date 2018/11/14 10:51
 */
@SpringBootApplication
public class UidApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(UidApplication.class);
        ctx.getBean(RpcUidServer.class).start();
    }
}
