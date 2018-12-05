package com.tybest.seckill.lock.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

/**
 * @author tb
 * @date 2018/12/5 10:45
 */
@Slf4j
public class ZkLock {

    private static CuratorFramework client;
    private static InterProcessMutex mutex;

    public void start(String host, int port) {
        log.info("start zookeeper connection");
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        client = CuratorFrameworkFactory.newClient(host+":"+port,retryPolicy);
        client.start();
        mutex = new InterProcessMutex(client, "/curator/lock");
        log.info("start zookeeper connection completely");
    }


    public static boolean lock(long time, TimeUnit unit) {
        try {
            return mutex.acquire(time,unit);
        } catch (Exception e) {
            log.error("try get zk lock failed",e);
        }
        return false;
    }

    public static void unlock() {
        try {
            mutex.release();
        } catch (Exception e) {
            log.error("try release zk lock failed",e);
        }
    }
}
