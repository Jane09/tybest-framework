package com.tybest.crawler.spider.impl;

import com.tybest.crawler.config.Config;
import com.tybest.crawler.config.UserAgent;
import com.tybest.crawler.parser.impl.PictureParser;
import com.tybest.crawler.pipeline.Pipeline;
import com.tybest.crawler.request.Request;
import com.tybest.crawler.request.RequestUtils;
import com.tybest.crawler.response.Response;
import com.tybest.crawler.response.Result;
import com.tybest.crawler.spider.Spider;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author tb
 * @date 2018/11/27 16:13
 */
@Slf4j
public class MeiziSpider extends Spider {

    private String storageDir = "D:/spider/";

    public MeiziSpider(String name) {
        super(name);
        this.startUrls(
                "http://www.meizitu.com/a/pure.html",
                "http://www.meizitu.com/a/cute.html",
                "http://www.meizitu.com/a/sexy.html",
                "http://www.meizitu.com/a/fuli.html",
                "http://www.meizitu.com/a/legs.html");
    }

    @Override
    public void onStart(Config config) {
        this.addPipeline((Pipeline<List<String>>) (item, request) -> {
            item.forEach(imgUrl -> {
                log.info("开始下载: {}", imgUrl);
                RequestUtils.loadImage(imgUrl, storageDir);
            });

            log.info("[{}] 图片下载 OJ8K.", request.getUrl());
        });

        this.requests.forEach(this::resetRequest);
    }

    @Override
    protected Result parse(Response response) {
        Result result = new Result<>();
        Elements elements = response.body().css("#maincontent > div.inWrap > ul > li:nth-child(1) > div > div > a");
        log.info("elements size: {}", elements.size());

        List<Request> requests = elements.stream()
                .map(element -> element.attr("href"))
                .map(href -> MeiziSpider.this.makeRequest(href, new PictureParser()))
                .map(this::resetRequest)
                .collect(Collectors.toList());
        result.addRequests(requests);

        // 获取下一页 URL
        Optional<Element> nextEl = response.body().css("#wp_page_numbers > ul > li > a").stream().filter(element -> "下一页".equals(element.text())).findFirst();
        if (nextEl.isPresent()) {
            String nextPageUrl = "http://www.meizitu.com/a/" + nextEl.get().attr("href");
            Request<String> nextReq = MeiziSpider.this.makeRequest(nextPageUrl, this::parse);
            result.addRequest(this.resetRequest(nextReq));
        }
        return result;
    }

    private Request resetRequest(Request request) {
        request.contentType("text/html; charset=gb2312");
        request.charset("gb2312");
        return request;
    }

}
