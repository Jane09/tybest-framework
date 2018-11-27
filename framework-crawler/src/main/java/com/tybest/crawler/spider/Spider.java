package com.tybest.crawler.spider;

import com.tybest.crawler.config.Config;
import com.tybest.crawler.event.EventManager;
import com.tybest.crawler.event.EventType;
import com.tybest.crawler.parser.Parser;
import com.tybest.crawler.pipeline.Pipeline;
import com.tybest.crawler.request.Request;
import com.tybest.crawler.response.Response;
import com.tybest.crawler.response.Result;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author tb
 * @date 2018/11/27 12:00
 */
@Getter
@Setter
public abstract class Spider {

    protected String name;
    protected Config config;
    protected List<String>   startUrls = new ArrayList<>();
    protected List<Pipeline> pipelines = new ArrayList<>();
    protected List<Request>  requests  = new ArrayList<>();

    public Spider(String name) {
        this.name = name;
        EventManager.registerEvent(EventType.SPIDER_STARTED, this::onStart);
    }

    public Spider startUrls(String... urls) {
        this.startUrls.addAll(Arrays.asList(urls));
        return this;
    }

    /**
     * 爬虫启动前执行
     */
    public void onStart(Config config) {
    }

    /**
     * 添加 Pipeline 处理
     */
    protected <T> Spider addPipeline(Pipeline<T> pipeline) {
        this.pipelines.add(pipeline);
        return this;
    }

    /**
     * 构建一个Request
     */
    public <T> Request<T> makeRequest(String url) {
        return makeRequest(url, this::parse);
    }

    public <T> Request<T> makeRequest(String url, Parser<T> parser) {
        return new Request(this, url, parser);
    }

    /**
     * 解析 DOM
     */
    protected abstract <T> Result<T> parse(Response response);

    protected void resetRequest(Consumer<Request> requestConsumer) {
        this.resetRequest(this.requests, requestConsumer);
    }

    protected void resetRequest(List<Request> requests, Consumer<Request> requestConsumer) {
        requests.forEach(requestConsumer::accept);
    }


}
