package com.tybest.crawler.downloader;

import com.tybest.crawler.request.Request;
import com.tybest.crawler.request.RequestUtils;
import com.tybest.crawler.response.Response;
import com.tybest.crawler.scheduler.Scheduler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tb
 * @date 2018/11/27 11:49
 */
@Slf4j
public class Downloader implements Runnable {

    private static final String GET="get";
    private static final String POST = "post";

    private final Scheduler scheduler;
    private final Request request;

    public Downloader(Scheduler scheduler, Request request) {
        this.scheduler = scheduler;
        this.request = request;
    }

    @Override
    public void run() {
        if(GET.equalsIgnoreCase(request.getMethod())){
            scheduler.addResponse(new Response(request, RequestUtils.doGet(request.getUrl())));
        }
        if(POST.equalsIgnoreCase(request.getMethod())) {
            scheduler.addResponse(new Response(request,RequestUtils.doPost(request.getUrl())));
        }
    }
}
