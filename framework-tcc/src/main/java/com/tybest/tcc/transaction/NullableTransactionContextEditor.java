package com.tybest.tcc.transaction;

import java.lang.reflect.Method;

/**
 * @author tb
 * @date 2018/12/10 11:29
 */
public class NullableTransactionContextEditor implements TransactionContextEditor {


    @Override
    public TransactionContext get(Object target, Method method, Object... args) {
        return null;
    }

    @Override
    public void set(TransactionContext context, Object target, Method method, Object... args) {

    }
}
