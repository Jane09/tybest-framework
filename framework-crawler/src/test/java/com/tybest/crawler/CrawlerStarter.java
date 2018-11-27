package com.tybest.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tb
 * @date 2018/11/14 11:06
 */
public class CrawlerStarter {

    public static void main(String[] args) throws IOException {
        String url = "http://www.hiphop8.com/city/hubei/jianghan.php";
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.body().getElementsByTag("a");
        List<String> subUrls = new ArrayList<>();
        Map<String,Object> result = new HashMap<>();
        List<String> duration = new ArrayList<>();
        elements.forEach(el -> {
            String targetUrl = el.attr("href");
            if(targetUrl.startsWith("../..")) {
                targetUrl = targetUrl.replace("../..","http://www.hiphop8.com");
                subUrls.add(targetUrl);
            }
            System.out.println(el.html());
        });

        subUrls.forEach(href -> {
            try {
                System.out.println(href);
                Document sd = Jsoup.connect(href).get();
                Elements els = sd.body().getElementsByTag("a");
                els.forEach(el -> {
                    System.out.println(el.html());
                });
            } catch (IOException e) {
                //skip
            }

        });
    }
}
