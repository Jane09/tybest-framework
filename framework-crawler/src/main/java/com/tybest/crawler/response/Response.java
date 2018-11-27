package com.tybest.crawler.response;

import com.tybest.crawler.request.Request;
import lombok.Getter;

/**
 * @author tb
 * @date 2018/11/27 13:41
 */
public class Response {

    @Getter
    private Request request;
    private StringBody    body;

    public Response(Request request, String body) {
        this.request = request;
        this.body = new StringBody(body);
    }

    public StringBody body() {
        return body;
    }
}
