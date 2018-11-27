package com.tybest.crawler.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import us.codecraft.xsoup.XElements;
import us.codecraft.xsoup.Xsoup;

/**
 * @author tb
 * @date 2018/11/27 15:43
 */
@AllArgsConstructor
public class StringBody {

    @Getter
    private String body;


    public Elements css(String css) {
        return Jsoup.parse(this.body).select(css);
    }

    public XElements xpath(String xpath) {
        return Xsoup.compile(xpath).evaluate(Jsoup.parse(this.body));
    }

}
