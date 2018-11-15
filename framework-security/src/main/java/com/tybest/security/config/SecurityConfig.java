package com.tybest.security.config;

import lombok.Getter;
import lombok.Setter;

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


}
