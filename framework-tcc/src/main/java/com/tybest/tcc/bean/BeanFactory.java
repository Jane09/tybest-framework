package com.tybest.tcc.bean;

public interface BeanFactory {

    /**
     * 获取Bean实例
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T getBean(Class<T> clazz);

    /**
     * 判断是否是factory
     * @param clazz
     * @param <T>
     * @return
     */
    <T> boolean isFactoryOf(Class<T> clazz);
}
