package com.tybest.crawler.spider;

import com.tybest.crawler.config.Config;
import com.tybest.crawler.scheduler.Scheduler;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author tb
 * @date 2018/11/27 13:55
 */
public class SpiderStater {


    private List<Spider> spiders;
    private Config config;
    private boolean         isRunning;
    private Scheduler scheduler;
    private ExecutorService executorService;

    
}
