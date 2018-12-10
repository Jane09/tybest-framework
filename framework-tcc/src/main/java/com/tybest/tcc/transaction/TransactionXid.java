package com.tybest.tcc.transaction;

import com.tybest.tcc.utils.IdUtils;

import javax.transaction.xa.Xid;
import java.io.Serializable;
import java.util.UUID;

/**
 * @author tb
 * @date 2018/12/7 15:27
 */
public class TransactionXid implements Xid, Serializable {


    private int formatId = 1;

    private byte[] globalTransactionId;
    private byte[] branchQualifier;

    public TransactionXid(){
        this(IdUtils.uuid2bytes(UUID.randomUUID()),IdUtils.uuid2bytes(UUID.randomUUID()));
    }

    public TransactionXid(byte[] globalTransactionId) {
        this(globalTransactionId,IdUtils.uuid2bytes(UUID.randomUUID()));
    }

    public TransactionXid(byte[] globalTransactionId, byte[] branchQualifier) {
        this.globalTransactionId = globalTransactionId;
        this.branchQualifier = branchQualifier;
    }

    @Override
    public int getFormatId() {
        return this.formatId;
    }

    @Override
    public byte[] getGlobalTransactionId() {
        return this.globalTransactionId;
    }

    @Override
    public byte[] getBranchQualifier() {
        return this.branchQualifier;
    }
}
