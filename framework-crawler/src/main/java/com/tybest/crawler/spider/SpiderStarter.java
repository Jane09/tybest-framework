package com.tybest.crawler.spider;

import com.tybest.crawler.config.Config;
import com.tybest.crawler.downloader.Downloader;
import com.tybest.crawler.event.EventManager;
import com.tybest.crawler.event.EventType;
import com.tybest.crawler.exception.SpiderException;
import com.tybest.crawler.parser.Parser;
import com.tybest.crawler.pipeline.Pipeline;
import com.tybest.crawler.request.Request;
import com.tybest.crawler.response.Response;
import com.tybest.crawler.response.Result;
import com.tybest.crawler.scheduler.Scheduler;
import com.tybest.crawler.utils.NamedThreadFactory;
import com.tybest.crawler.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author tb
 * @date 2018/11/27 13:55
 */
@Slf4j
public class SpiderStarter {


    private List<Spider> spiders;
    private Config config;
    private volatile boolean isRunning = false;
    private Scheduler scheduler;
    private ExecutorService executorService;

    public SpiderStarter(List<Spider> spiders, Config config) {
        this.spiders = spiders;
        this.scheduler = new Scheduler();
        this.config = config;
        this.executorService = new ThreadPoolExecutor(config.parallelThreads(),config.parallelThreads(),0,
                TimeUnit.SECONDS,
                config.queueSize() ==0?new SynchronousQueue<>():(config.queueSize()<0?new LinkedBlockingDeque<>():
                        new LinkedBlockingDeque<>(config.queueSize())),
                new NamedThreadFactory("spider-task"));
    }

    public void start() {
        if(isRunning) {
            throw new SpiderException("程序已启动");
        }
        isRunning = true;
        EventManager.fireEvent(EventType.GLOBAL_STARTED,config);
        spiders.forEach(spider -> {
            Config cnf = this.config.clone();
            log.info("Spider [{}] 启动...", spider.getName());
            log.info("Spider [{}] 配置 [{}]", spider.getName(), cnf);
            spider.setConfig(cnf);
            List<Request> requests = spider.getStartUrls().stream()
                    .map(spider::makeRequest).collect(Collectors.toList());
            spider.getRequests().addAll(requests);
            scheduler.addRequests(requests);
            EventManager.fireEvent(EventType.SPIDER_STARTED, cnf);
        });

        ExecutorService executor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<>(1024), new NamedThreadFactory("spider-download"),
                (r, executor1) -> log.error("Request Queue is full and the current request will be aborted."));
        executor.execute(() -> {
            while (isRunning) {
                if(!scheduler.hasRequest()){
                    Utils.sleep(100);
                    continue;
                }
                Request request = scheduler.nextRequest();
                executorService.submit(new Downloader(scheduler, request));
                Utils.sleep(request.getSpider().getConfig().delay());
            }
        });
        complete();
    }

    private void complete(){
        while (isRunning) {
            if (!scheduler.hasResponse()) {
                Utils.sleep(100);
                continue;
            }
            Response response = scheduler.nextResponse();
            Parser parser   = response.getRequest().getParser();
            if (null != parser) {
                Result<?> result   = parser.parse(response);
                List<Request> requests = result.getRequests();
                if (!Utils.isEmpty(requests)) {
                    requests.forEach(scheduler::addRequest);
                }
                if (null != result.getItem()) {
                    List<Pipeline> pipelines = response.getRequest().getSpider().getPipelines();
                    pipelines.forEach(pipeline -> pipeline.process(result.getItem(), response.getRequest()));
                }
            }
        }
    }

    public void stop(){
        isRunning = false;
        scheduler.clear();
        log.info("爬虫已经停止.");
    }
}
