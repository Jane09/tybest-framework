package com.tybest.security.config;

/**
 * @author tb
 * @date 2018/11/15 16:01
 */
public class TAuthenticateConfigurer {

    private final SecurityConfig securityConfig;

    public TAuthenticateConfigurer(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
    }
}
