package com.tybest.security.config;

import com.tybest.security.config.metadata.EncoderConfig;
import com.tybest.security.config.metadata.JwtTokenConfig;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tb
 * @date 2018/11/15 15:58
 */
@Getter
@Setter
public class SecurityConfig {

    public static final String CONFIG_PREFIX = "tybest.t-security";
    public static final String TOKEN_TYPE_JWT = "jwt";
    public static final String TOKEN_TYPE_UUID = "uuid";
    public static final String CACHE_TYPE_GUAVA = "guava";
    public static final String CACHE_TYPE_REDIS = "redis";


    private boolean supervisorGranted = true;

    private List<String> ignoreUrls = new ArrayList<>();

    private JwtTokenConfig jwtToken = new JwtTokenConfig();


    private EncoderConfig encoder = new EncoderConfig();



}
