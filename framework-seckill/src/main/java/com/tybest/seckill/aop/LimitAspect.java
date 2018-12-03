package com.tybest.seckill.aop;

import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author tb
 * @date 2018/12/3 11:48
 */
@Component
@Scope
@Aspect
public class LimitAspect {

    private static RateLimiter rateLimiter = RateLimiter.create(5.0);

    @Pointcut("@annotation(com.tybest.seckill.aop.ServiceLimit)")
    public void serviceAspect() {}

    @Around("serviceAspect()")
    public  Object around(ProceedingJoinPoint joinPoint) {
        Object obj = null;
        try {
            if(rateLimiter.tryAcquire()){
                obj = joinPoint.proceed();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return obj;
    }
}
