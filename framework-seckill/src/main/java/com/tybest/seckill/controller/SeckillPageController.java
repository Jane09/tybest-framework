package com.tybest.seckill.controller;

import com.alibaba.fastjson.JSONObject;
import com.tybest.seckill.entity.Seckill;
import com.tybest.seckill.model.Result;
import com.tybest.seckill.queue.redis.RedisOperator;
import com.tybest.seckill.queue.redis.RedisProducer;
import com.tybest.seckill.service.SeckillService;
import com.tybest.seckill.utils.HttpClient;
import com.tybest.seckill.utils.IPUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author tb
 * @date 2018/12/4 14:23
 */
@Api(tags = "秒杀商品")
@RestController
@RequestMapping("/seckillPage")
@RequiredArgsConstructor
public class SeckillPageController {

    @Value("${qq.captcha.url}")
    private String url;
    @Value("${qq.captcha.aid}")
    private String aid;
    @Value("${qq.captcha.AppSecretKey}")
    private String appSecretKey;

    private final SeckillService seckillService;
    private final RedisProducer redisProducer;


    @ApiOperation(value = "秒杀商品列表", nickname = "小柒2012")
    @PostMapping("/list")
    public Result list() {
        //返回JSON数据、前端VUE迭代即可
        List<Seckill> list = seckillService.getSeckillList();
        return Result.ok(list);
    }

    @RequestMapping("/startSeckill")
    public Result  startSeckill(String ticket, String randstr, HttpServletRequest request) {
        HttpMethod method =HttpMethod.POST;
        MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();
        params.add("aid", aid);
        params.add("AppSecretKey", appSecretKey);
        params.add("Ticket", ticket);
        params.add("Randstr", randstr);
        params.add("UserIP", IPUtils.getIpAddr(request));
        String msg = HttpClient.client(url,method,params);
        JSONObject json = JSONObject.parseObject(msg);
        String response = (String) json.get("response");
        if("100".equals(response)){
            redisProducer.sendMessage(RedisOperator.CHANNEL,"1000;1");
            return Result.ok();
        }else{
            return Result.error("验证失败");
        }
    }
}
