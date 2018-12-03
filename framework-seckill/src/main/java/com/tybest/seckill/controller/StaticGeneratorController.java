package com.tybest.seckill.controller;

import com.tybest.seckill.model.Result;
import com.tybest.seckill.service.GenerateHtmlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tb
 * @date 2018/12/3 13:23
 */
@Api(tags ="生成静态商品页")
@RestController
@RequestMapping("/createHtml")
@RequiredArgsConstructor
@Slf4j
public class StaticGeneratorController {

    private final GenerateHtmlService generateHtmlService;

    @ApiOperation(value="生成静态商品页",httpMethod = "POST")
    @PostMapping("/start")
    public Result start(){
        log.info("生成秒杀活动静态页");
        return generateHtmlService.generator();
    }
}
