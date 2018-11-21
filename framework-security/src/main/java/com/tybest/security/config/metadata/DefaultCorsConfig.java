package com.tybest.security.config.metadata;

/**
 * @author tb
 * @date 2018/11/21 12:02
 */
public class DefaultCorsConfig extends CorsConfig {

    private static final String DEFAULT_CORS_ORIGINS = "*";

    public DefaultCorsConfig() {
        super(DEFAULT_CORS_ORIGINS);
    }
}
