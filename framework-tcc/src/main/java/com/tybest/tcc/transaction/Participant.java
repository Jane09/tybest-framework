package com.tybest.tcc.transaction;

import java.io.Serializable;

/**
 * 参与者
 * @author tb
 * @date 2018/12/10 16:55
 */
public class Participant implements Serializable {

    private TransactionXid xid;

    private InvocationContext confirmInvocationContext;

    private InvocationContext cancelInvocationContext;


    public void commit() {

    }


    public void rollback() {

    }
}
