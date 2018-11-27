package com.tybest.crawler.parser.impl;

import com.tybest.crawler.parser.Parser;
import com.tybest.crawler.response.Response;
import com.tybest.crawler.response.Result;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tb
 * @date 2018/11/27 16:15
 */
public class PictureParser implements Parser<List<String>> {

    @Override
    public Result parse(Response response) {
        Elements elements = response.body().css("#picture > p > img");
        List<String> src      = elements.stream().map(element -> element.attr("src")).collect(Collectors.toList());
        return new Result<>(src);
    }
}
