package com.tybest.crawler.spider;

import com.tybest.crawler.config.Config;
import com.tybest.crawler.pipeline.Pipeline;
import com.tybest.crawler.request.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tb
 * @date 2018/11/27 12:00
 */
public abstract class Spider {

    protected String name;
    protected Config config;
    protected List<String> startUrls = new ArrayList<>();
    protected List<Pipeline> pipelines = new ArrayList<>();
    protected List<Request>  requests  = new ArrayList<>();


}
