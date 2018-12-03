package com.tybest.seckill.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author tb
 * @date 2018/12/3 11:51
 */
@Component
@Scope
@Aspect
@Order(1)
public class LockAspect {

    private static Lock lock = new ReentrantLock(true);

    @Pointcut("@annotation(com.tybest.seckill.aop.ServiceLock)")
    public void lockAspect() {}

    @Around("lockAspect()")
    public  Object around(ProceedingJoinPoint joinPoint) {
        lock.lock();
        Object obj;
        try {
            obj = joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally{
            lock.unlock();
        }
        return obj;
    }
}
