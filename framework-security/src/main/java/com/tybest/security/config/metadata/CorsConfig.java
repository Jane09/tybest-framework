package com.tybest.security.config.metadata;

import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * @author tb
 * @date 2018/11/21 11:58
 */
@RequiredArgsConstructor
public class CorsConfig {

    private final String allowedOrigins;


    public List<String> getAllowedOrigins(){
        Assert.hasText(allowedOrigins.trim(),"The allowed origins can not be null!");
        return Arrays.asList(this.allowedOrigins.split(","));
    }
}
