package com.tybest.crawler.response;

import com.tybest.crawler.request.Request;
import lombok.Getter;

import java.io.InputStream;

/**
 * @author tb
 * @date 2018/11/27 13:41
 */
public class Response {

    @Getter
    private Request request;
    private Body    body;

    public Response(Request request, InputStream inputStream) {
        this.request = request;
        this.body = new Body(inputStream, request.charset());
    }

    public Body body() {
        return body;
    }
}
