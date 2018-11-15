package com.tybest.security;

import com.tybest.security.config.SecurityConfig;
import com.tybest.security.config.TAuthenticateConfigurer;
import com.tybest.security.service.TAccountService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 *
 * 基础引用
 * @author tb
 * @date 2018/11/15 15:37
 */
public abstract class TSecurityConfigurerAdatper extends WebSecurityConfigurerAdapter implements InitializingBean {

    /**
     * 通过登录名加载用户基础信息
     */
    protected final TAccountService accountService;
    protected final AuthenticationManager authenticationManager;
    protected final TAuthenticateConfigurer authenticateConfigurer;

    protected TSecurityConfigurerAdatper(TAccountService accountService, AuthenticationManager authenticationManager, TAuthenticateConfigurer authenticateConfigurer) {
        this.accountService = accountService;
        this.authenticationManager = authenticationManager;
        this.authenticateConfigurer = authenticateConfigurer;
    }

    protected abstract void additionalConfigure(AuthenticationManagerBuilder auth);

    protected abstract void additionalConfigure(HttpSecurity http);


    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        additionalConfigure(auth);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       additionalConfigure(http);
    }


    @Bean
    public AuthenticationManager getAuthenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @ConfigurationProperties(prefix = SecurityConfig.CONFIG_PREFIX)
    public SecurityConfig getConfigBean() {
        return new SecurityConfig();
    }

    @Bean
    public TAuthenticateConfigurer authenticateConfigurer(SecurityConfig securityConfig) {
        return new TAuthenticateConfigurer(securityConfig);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        authenticateConfigurer.accountService(accountService).loginSuccessCallback(loginSuccessCallback).authenticationManager(authenticationManager);
    }
}
