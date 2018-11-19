package com.tybest.security.web.auth;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tb
 * @date 2018/11/19 14:09
 */
@Component
public class TAuthExceptionHandler implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    @Override
    public synchronized void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        TAuthExceptionHandler.applicationContext = applicationContext;
    }

    public static void handle(HttpServletRequest request, HttpServletResponse response, Throwable failed) {

    }
}
