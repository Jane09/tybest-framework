package com.tybest.tcc.transaction;

import com.tybest.tcc.utils.IdUtils;

import javax.transaction.xa.Xid;
import java.io.Serializable;
import java.util.Arrays;
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


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.getFormatId();
        result = prime * result + Arrays.hashCode(branchQualifier);
        result = prime * result + Arrays.hashCode(globalTransactionId);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        }
        TransactionXid other = (TransactionXid) obj;
        if (this.getFormatId() != other.getFormatId()) {
            return false;
        } else if (!Arrays.equals(branchQualifier, other.branchQualifier)) {
            return false;
        } else {
            return Arrays.equals(globalTransactionId, other.globalTransactionId);
        }
    }

    @Override
    protected TransactionXid clone() {
        byte[] cloneGlobalTransactionId = null;
        byte[] cloneBranchQualifier = null;
        if (globalTransactionId != null) {
            cloneGlobalTransactionId = new byte[globalTransactionId.length];
            System.arraycopy(globalTransactionId, 0, cloneGlobalTransactionId, 0, globalTransactionId.length);
        }
        if (branchQualifier != null) {
            cloneBranchQualifier = new byte[branchQualifier.length];
            System.arraycopy(branchQualifier, 0, cloneBranchQualifier, 0, branchQualifier.length);
        }
        return new TransactionXid(cloneGlobalTransactionId, cloneBranchQualifier);
    }

    @Override
    public String toString() {
        return IdUtils.bytes2uuid(this.globalTransactionId).toString() + ":" +
                IdUtils.bytes2uuid(this.branchQualifier).toString();
    }
}
