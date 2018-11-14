package com.tybest.base.utils;

import static java.lang.Thread.currentThread;

/**
 * @author tb
 * @date 2018/11/14 11:57
 */
public final class ClassUtils {

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = currentThread().getContextClassLoader();
        }catch (Throwable ex) {
            //do nonthing;
        }
        if(cl == null){
            cl = ClassUtils.class.getClassLoader();
            if(cl == null){
                try{
                    cl = ClassLoader.getSystemClassLoader();
                }catch (Throwable e) {
                    //do nothing
                }
            }
        }
        return cl;
    }
}
