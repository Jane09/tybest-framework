package com.tybest.oauth.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author tb
 * @date 2018/12/11 14:22
 */
public class OAuth2AuthenticationException extends AuthenticationException {


    public OAuth2AuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public OAuth2AuthenticationException(String msg) {
        super(msg);
    }
}
