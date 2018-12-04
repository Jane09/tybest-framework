package com.tybest.seckill.utils;

import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author tb
 * @date 2018/12/3 11:36
 */
@Component
public class SpringUtils implements ApplicationContextAware {

    @Getter
    private static ApplicationContext applicationContext;

    @Override
    @ParametersAreNonnullByDefault
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringUtils.applicationContext = applicationContext;
    }

    public static  <T> T getBean(Class<T> clazz){
        return  getApplicationContext().getBean(clazz);
    }
}
