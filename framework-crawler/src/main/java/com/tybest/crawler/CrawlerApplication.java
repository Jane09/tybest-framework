package com.tybest.crawler;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author tb
 * @date 2018/11/27 11:35
 */
@SpringBootApplication
public class CrawlerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(CrawlerApplication.class);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
