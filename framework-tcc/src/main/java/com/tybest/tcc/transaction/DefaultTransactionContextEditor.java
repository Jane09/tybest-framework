package com.tybest.tcc.transaction;

import java.lang.reflect.Method;

/**
 * @author tb
 * @date 2018/12/10 11:27
 */
public class DefaultTransactionContextEditor implements TransactionContextEditor {


    @Override
    public TransactionContext get(Object target, Method method, Object... args) {
        int position = getPosition(method.getParameterTypes());
        if(position >= 0){
            return (TransactionContext) args[position];
        }
        return null;
    }

    @Override
    public void set(TransactionContext context, Object target, Method method, Object... args) {
        int position = getPosition(method.getParameterTypes());
        if(position >= 0){
            args[position] = context;
        }
    }


    private int getPosition(Class<?>[] parameterTypes) {
        int position = -1;
        for(int i=0,len=parameterTypes.length;i<len;i++){
            if(parameterTypes[i].equals(TransactionContext.class)) {
                return i;
            }
        }
        return position;
    }
}
