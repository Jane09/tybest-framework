package com.tybest.crawler.spider.impl;

import com.tybest.crawler.config.Config;
import com.tybest.crawler.pipeline.Pipeline;
import com.tybest.crawler.request.Request;
import com.tybest.crawler.response.Response;
import com.tybest.crawler.response.Result;
import com.tybest.crawler.spider.Spider;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tb
 * @date 2018/11/27 16:07
 */
@Slf4j
public class DoubaiSpider extends Spider {


    public DoubaiSpider(String name) {
        super(name);
        this.startUrls(
                "https://movie.douban.com/tag/爱情",
                "https://movie.douban.com/tag/喜剧",
                "https://movie.douban.com/tag/动画",
                "https://movie.douban.com/tag/动作",
                "https://movie.douban.com/tag/史诗",
                "https://movie.douban.com/tag/犯罪");
    }

    @Override
    public void onStart(Config config) {
        this.addPipeline((Pipeline<List<String>>) (item, request) -> log.info("保存到文件: {}", item));
    }

    @Override
    @SuppressWarnings({"unchecked"})
    protected Result<List<String>> parse(Response response) {
        Result<List<String>> result   = new Result<>();
        Elements elements = response.body().css("#content table .pl2 a");
        List<String> titles = elements.stream().map(Element::text).collect(Collectors.toList());
        result.setItem(titles);

        // 获取下一页 URL
        Elements nextEl = response.body().css("#content > div > div.article > div.paginator > span.next > a");
        if (null != nextEl && nextEl.size() > 0) {
            String  nextPageUrl = nextEl.get(0).attr("href");
            Request nextReq     = this.makeRequest(nextPageUrl, this::parse);
            result.addRequest(nextReq);
        }
        return result;
    }
}
