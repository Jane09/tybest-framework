package com.tybest.crawler.pipeline;

import com.tybest.crawler.request.Request;

public interface Pipeline<T> {

    void process(T item, Request request);
}
