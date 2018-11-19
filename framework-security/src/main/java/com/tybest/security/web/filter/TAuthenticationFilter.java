package com.tybest.security.web.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * @author tb
 * @date 2018/11/19 14:23
 */
public abstract class TAuthenticationFilter<T extends Authentication> extends TClientAuthenticationFilter {


    public TAuthenticationFilter(RequestMatcher requestMatcher) {
        super(requestMatcher);
    }
}
