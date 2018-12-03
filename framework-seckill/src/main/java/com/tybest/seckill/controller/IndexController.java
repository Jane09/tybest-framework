package com.tybest.seckill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author tb
 * @date 2018/12/3 11:35
 */
@Controller
public class IndexController {

    @RequestMapping("{url}.shtml")
    public String page(@PathVariable("url") String url) {
        return  url;
    }

    @RequestMapping("{module}/{url}.shtml")
    public String page(@PathVariable("module") String module,@PathVariable("url") String url) {
        return module + "/" + url;
    }
}
