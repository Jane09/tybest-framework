package com.tybest.crawler;

import com.tybest.crawler.config.Config;
import com.tybest.crawler.spider.Spider;
import com.tybest.crawler.spider.SpiderStarter;
import com.tybest.crawler.spider.impl.DoubaiSpider;
import com.tybest.crawler.spider.impl.MeiziSpider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

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
    public void run(String... args) {
        List<Spider> spiders = new ArrayList<>();
        spiders.add(new DoubaiSpider("doubai"));
        spiders.add(new MeiziSpider("meizi"));
        SpiderStarter starter = new SpiderStarter(spiders, Config.me());
        starter.start();
    }
}
