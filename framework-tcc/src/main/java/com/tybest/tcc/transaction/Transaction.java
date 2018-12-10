package com.tybest.tcc.transaction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.transaction.xa.Xid;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 事务
 * @author tb
 * @date 2018/12/10 16:55
 */
@NoArgsConstructor
public class Transaction implements Serializable {

    private TransactionXid xid;

    @Getter
    @Setter
    private TransactionStatus status;
    @Getter
    private TransactionType type;

    private AtomicInteger retries = new AtomicInteger(0);
    @Getter
    @Setter
    private Date createTime = Calendar.getInstance().getTime();
    @Getter
    @Setter
    private Date lastUpdateTime = Calendar.getInstance().getTime();
    @Getter
    @Setter
    private long version = 1;
    @Getter
    private List<Participant> participants = new ArrayList<>();
    @Getter
    private Map<String,Object> attachments = new ConcurrentHashMap<>(8);

    public Transaction(TransactionContext context) {
        this.xid = context.getXid();
        this.status = TransactionStatus.TRYING;
        this.type = TransactionType.BRANCH;
    }

    public Transaction(TransactionType type) {
        this.type = type;
        this.xid = new TransactionXid();
        this.status = TransactionStatus.TRYING;
    }

    public void addParticipant(Participant participant) {
        this.participants.add(participant);
    }

    public Xid getXid() {
        return this.xid.clone();
    }

    public void commit() {
        participants.forEach(Participant::commit);
    }

    public void rollback() {
        participants.forEach(Participant::rollback);
    }

    public void incrRetries() {
        this.retries.incrementAndGet();
    }

    public int getRetries() {
        return this.retries.get();
    }

    public void setRetries(int retries) {
        this.retries.set(retries);
    }

    public void incrVersion() {
        this.version ++;
    }
}
