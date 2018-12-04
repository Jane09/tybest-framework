package com.tybest.seckill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author tb
 * @date 2018/12/3 11:35
 */
@Controller
public class IndexController {

    @RequestMapping(value = "{url}.shtml",method = RequestMethod.GET)
    public String page(@PathVariable("url") String url) {
        return  url;
    }

    @RequestMapping(value = "{module}/{url}.shtml",method = RequestMethod.GET)
    public String page(@PathVariable("module") String module,@PathVariable("url") String url) {
        return module + "/" + url;
    }
}
