package com.tybest.security.web.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tb
 * @date 2018/11/19 14:19
 */
public abstract class TClientAuthenticationFilter<T extends Authentication> extends AbstractAuthenticationProcessingFilter {

    public TClientAuthenticationFilter(RequestMatcher requestMatcher) {
        super(requestMatcher);


    }

    protected abstract T buildAuthentication(HttpServletRequest request, HttpServletResponse response);

}
