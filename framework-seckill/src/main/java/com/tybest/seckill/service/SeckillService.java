package com.tybest.seckill.service;

import com.tybest.seckill.aop.ServiceLimit;
import com.tybest.seckill.aop.ServiceLock;
import com.tybest.seckill.entity.Seckill;
import com.tybest.seckill.entity.SuccessKilled;
import com.tybest.seckill.model.Result;
import com.tybest.seckill.model.StateEnum;
import com.tybest.seckill.repository.DynamicNativeQuery;
import com.tybest.seckill.repository.SeckillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author tb
 * @date 2018/12/3 13:54
 */
@Service
@RequiredArgsConstructor
public class SeckillService {

    private Lock lock = new ReentrantLock(true);

    private final DynamicNativeQuery dynamicNativeQuery;
    private final SeckillRepository seckillRepository;


    public List<Seckill> getSeckillList() {
        return seckillRepository.findAll();
    }

    private Seckill findById(long seckillId) {
        return seckillRepository.findById(seckillId).orElse(null);
    }

    public long getSeckillCount(long seckillId) {
        String sql = "SELECT count(*) FROM success_killed WHERE seckill_id=?";
        Object result = dynamicNativeQuery.nativeQueryObject(sql,seckillId);
        return ((BigInteger)result).longValue();
    }

    @Transactional(rollbackOn = Throwable.class)
    public void reset(long seckillId) {
        String sql = "DELETE FROM  success_killed WHERE seckill_id=?";
        dynamicNativeQuery.nativeExecuteUpdate(sql,seckillId);
        sql = "UPDATE seckill SET number =100 WHERE seckill_id=?";
        dynamicNativeQuery.nativeExecuteUpdate(sql,seckillId);
    }

    /**
     * 限流-少卖很多
     * 不限流-超卖
     */
    @ServiceLimit
    @Transactional(rollbackOn = Throwable.class)
    public Result seckillOne(long seckillId, long userId) {
        return seckill(seckillId,userId);
    }


    @Transactional(rollbackOn = Throwable.class)
    public Result seckillSeq(long seckillId, long userId) {
        return seckill(seckillId,userId);
    }

    /**
     * 多卖
     */
    @Transactional(rollbackOn = Throwable.class)
    public Result seckillLock(long seckillId,long userId) {
        try{
            lock.lock();
            return seckill(seckillId,userId);
        }finally {
            lock.unlock();
        }
    }

    /**
     * 正常
     */
    @ServiceLock
    @Transactional(rollbackOn = Throwable.class)
    public Result seckillAopLock(long seckillId,long userId) {
        return seckill(seckillId,userId);
    }

    /**
     * 少卖
     */
    @ServiceLimit
    @Transactional(rollbackOn = Throwable.class)
    public Result seckillDbOne(long seckillId, long userId) {
        String sql = "SELECT number FROM seckill WHERE seckill_id=? FOR UPDATE";
        return seckill(sql,seckillId,userId);
    }

    /**
     * 1 件 = 正常
     */
    @Transactional(rollbackOn = Throwable.class)
    public Result seckillDbTwo(long seckillId, long userId) {
        String sql = "UPDATE seckill  SET number=number-1 WHERE seckill_id=? AND number>0";
        int count = dynamicNativeQuery.nativeExecuteUpdate(sql, seckillId);
        if(count <= 0){
            return Result.error(StateEnum.FINISH);
        }
        newOrder(seckillId,userId);
        return Result.ok(StateEnum.SUCCESS);
    }

    /**
     * 正常
     */
    @Transactional(rollbackOn = Throwable.class)
    public Result seckillOptLock(long seckillId,long userId,long number) {
        Seckill kill = findById(seckillId);
        if(null == kill){
            return Result.error(StateEnum.DATE_REWRITE);
        }
        if(kill.getNumber()< number) {
            return Result.error(StateEnum.FINISH);
        }
        String sql = "UPDATE seckill  SET number=number-?,version=version+1 WHERE seckill_id=? AND version = ?";
        int count = dynamicNativeQuery.nativeExecuteUpdate(sql, number,seckillId,kill.getVersion());
        if(count <= 0){
            return Result.error(StateEnum.FINISH);
        }
        newOrder(seckillId,userId);
        return Result.ok(StateEnum.SUCCESS);
    }


    @Transactional(rollbackOn = Throwable.class)
    public Result seckilRedisLock(long seckillId,long userId) {
        boolean res = false;
        try{
            lock.lock();
            return seckill(seckillId,userId);
        }finally {
            lock.unlock();
        }
    }




    private Result seckill(String sql,long seckillId, long userId) {
        Object num = dynamicNativeQuery.nativeQueryObject(sql,seckillId);
        Integer number = (Integer) num;
        if(number <= 0){
            return Result.error(StateEnum.FINISH);
        }
        sql = "UPDATE seckill  SET number=number-1 WHERE seckill_id=?";
        dynamicNativeQuery.nativeExecuteUpdate(sql,seckillId);
        newOrder(seckillId,userId);
        return Result.ok(StateEnum.SUCCESS);
    }

    private Result seckill(long seckillId, long userId) {
        String sql = "SELECT number FROM seckill WHERE seckill_id=?";
        return seckill(sql,seckillId,userId);
    }

    /**
     * 新增订单
     */
    private void newOrder(long seckillId, long userId) {
        SuccessKilled killed = new SuccessKilled();
        killed.setSeckillId(seckillId);
        killed.setUserId(userId);
        killed.setState((short)0);
        killed.setCreateTime(new Timestamp(System.currentTimeMillis()));
        dynamicNativeQuery.save(killed);
    }
}
