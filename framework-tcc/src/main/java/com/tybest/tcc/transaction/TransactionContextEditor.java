package com.tybest.tcc.transaction;

import java.lang.reflect.Method;

/**
 * @author tb
 */
public interface TransactionContextEditor {

    /**
     * 获取事务上下文
     * @param target
     * @param method
     * @param args
     * @return
     */
    TransactionContext get(Object target, Method method, Object[] args);

    /**
     * 设置事务上下文
     * @param context
     * @param target
     * @param method
     * @param args
     */
    void set(TransactionContext context,Object target, Method method, Object[] args);
}
