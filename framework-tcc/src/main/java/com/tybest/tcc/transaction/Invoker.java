package com.tybest.tcc.transaction;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author tb
 * @date 2018/12/10 17:21
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Invoker {


    /**
     * 调用
     * @param transactionContext
     * @param invocationContext
     * @param transactionContextEditor
     */
    public static void invoke(TransactionContext transactionContext,InvocationContext invocationContext,Class<? extends TransactionContextEditor> transactionContextEditor) {

    }
}
