package com.tybest.security.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * @author tb
 * @date 2018/11/15 16:01
 */
public class TAuthenticateConfigurer {

    private final SecurityConfig securityConfig;

    public TAuthenticateConfigurer(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
    }

    /**
     * 配置url过滤
     * @param http
     * @throws Exception
     */
    public void configureHttpSecurity(HttpSecurity http) throws Exception {

    }

    /**
     * 配置AuthenticationProvider
     * @param auth
     */
    public void configureAuthenticationManagerBuilder(AuthenticationManagerBuilder auth) {

    }
}
