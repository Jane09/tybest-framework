package com.tybest.crawler.response;

import com.tybest.crawler.request.Request;
import com.tybest.crawler.utils.Utils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tb
 * @date 2018/11/27 12:03
 */
@Getter
@Setter
@NoArgsConstructor
public class Result<T> {

    private List<Request> requests = new ArrayList<>();
    private T item;

    public Result(T item) {
        this.item = item;
    }

    public Result addRequest(Request request) {
        this.requests.add(request);
        return this;
    }

    public Result addRequests(List<Request> requests) {
        if (!Utils.isEmpty(requests)) {
            this.requests.addAll(requests);
        }
        return this;
    }
}
