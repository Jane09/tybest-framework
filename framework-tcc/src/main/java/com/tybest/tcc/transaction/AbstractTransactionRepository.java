package com.tybest.tcc.transaction;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.tybest.tcc.exception.InternalException;
import lombok.Setter;

import javax.transaction.xa.Xid;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author tb
 * @date 2018/12/11 11:23
 */
public abstract class AbstractTransactionRepository implements TransactionRepository {

    @Setter
    private int expireDuration = 120;
    private Cache<Xid,Transaction> cache;

    public AbstractTransactionRepository(){
        cache = CacheBuilder.newBuilder().expireAfterAccess(expireDuration, TimeUnit.SECONDS).maximumSize(1000).build();
    }

    @Override
    public int create(Transaction transaction) {
        int result = doCreate(transaction);
        if(result > 0){
            cache.put(transaction.getXid(),transaction);
        }
        return result;
    }

    protected abstract int doCreate(Transaction transaction);

    @Override
    public int update(Transaction transaction) {
        int result = 0;
        try{
            result = doUpdate(transaction);
            if(result > 0){
                cache.put(transaction.getXid(),transaction);
            }else {
                throw new InternalException("optimistic lock failed");
            }
        }finally {
            if(result <= 0){
                cache.invalidate(transaction.getXid());
            }
        }
        return result;
    }

    protected abstract int doUpdate(Transaction transaction);

    @Override
    public int delete(Transaction transaction) {
        int result;
        try{
            result = doDelete(transaction);
        }finally {
            cache.invalidate(transaction.getXid());
        }
        return result;
    }


    protected abstract int doDelete(Transaction transaction);

    @Override
    public Transaction findByXid(TransactionXid xid) {
        Transaction transaction = cache.getIfPresent(xid);
        if(null == transaction){
            transaction = doFindOne(xid);
            if(null != transaction){
                cache.put(transaction.getXid(),transaction);
            }
        }
        return transaction;
    }

    protected abstract Transaction doFindOne(TransactionXid xid);

    @Override
    public List<Transaction> findAllUnmodifiedSince(Date date) {
        List<Transaction> transactions = doFindAllUnmodifiedSince(date);
        if(null != transactions) {
            transactions.forEach(transaction -> cache.put(transaction.getXid(),transaction));
        }
        return transactions;
    }


    protected abstract List<Transaction> doFindAllUnmodifiedSince(Date date);
}
