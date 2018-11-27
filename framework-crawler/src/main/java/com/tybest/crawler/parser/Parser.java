package com.tybest.crawler.parser;

import com.tybest.crawler.response.Response;
import com.tybest.crawler.response.Result;

public interface Parser<T> {

    Result<T> parse(Response response);
}
