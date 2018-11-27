package com.tybest.crawler.scheduler;

import com.tybest.crawler.request.Request;
import com.tybest.crawler.response.Response;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author tb
 * @date 2018/11/27 13:40
 */
public class Scheduler {

    private BlockingQueue<Request> pending = new LinkedBlockingQueue<>();
    private BlockingQueue<Response> result  = new LinkedBlockingQueue<>();

}
