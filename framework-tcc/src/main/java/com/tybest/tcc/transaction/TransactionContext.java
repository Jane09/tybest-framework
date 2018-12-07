package com.tybest.tcc.transaction;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author tb
 * @date 2018/12/7 15:42
 */
public class TransactionContext implements Serializable {

    @Getter
    @Setter
    private TransactionXid xid;
    @Getter
    @Setter
    private int status;

    @Getter
    private Map<String,String> attachments = new ConcurrentHashMap<>(16);

    public TransactionContext(){}

    public TransactionContext(TransactionXid xid,int status){
        this.xid = xid;
        this.status = status;
    }

    public void setAttachments(Map<String, String> attachments) {
        if(attachments != null) {
            this.attachments.putAll(attachments);
        }
    }
}
