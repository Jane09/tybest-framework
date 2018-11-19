package com.tybest.security.service;

import com.tybest.security.token.TokenProcessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.RememberMeServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tb
 * @date 2018/11/19 14:00
 */
public class TClientRememberMeService implements RememberMeServices {

    private final TokenProcessor tokenProcessor;

    public TClientRememberMeService(TokenProcessor tokenProcessor) {
        this.tokenProcessor = tokenProcessor;
    }

    @Override
    public Authentication autoLogin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return null;
    }

    @Override
    public void loginFail(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

    }

    @Override
    public void loginSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {

    }
}
