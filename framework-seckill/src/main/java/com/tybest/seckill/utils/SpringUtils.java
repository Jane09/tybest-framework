package com.tybest.seckill.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationContext;

/**
 * @author tb
 * @date 2018/12/3 11:36
 */
public class SpringUtils {

    @Setter
    @Getter
    private static ApplicationContext applicationContext;


    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }
}
