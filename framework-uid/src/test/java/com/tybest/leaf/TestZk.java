package com.tybest.leaf;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * @author tb
 * @date 2018/11/22 11:39
 */
public class TestZk {

    @Test
    public void test0() throws Exception {
        CuratorFramework conn = CuratorFrameworkFactory.builder()
                .connectString("localhost:2181")
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .sessionTimeoutMs(50000)
                .connectionTimeoutMs(3000)
                .build();
        conn.start();
        String data = "helloworld";
        conn.create()
                .forPath("/root", data.getBytes(StandardCharsets.UTF_8));
        conn.create().forPath("/root/node1",data.getBytes(StandardCharsets.UTF_8));

        System.out.println(conn.checkExists().forPath("/my/path") !=null);
        byte[] bb = conn.getData().forPath("/my/path");
        System.out.println(new String(bb));
        conn.delete().forPath("/my/path");
        System.out.println(conn.checkExists().forPath("/my/path") !=null);
    }


    @Test
    public void test() throws Exception {
        CuratorFramework conn = CuratorFrameworkFactory.builder()
                .connectString("localhost:2181")
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .sessionTimeoutMs(50000)
                .connectionTimeoutMs(3000)
                .build();
        conn.start();
        String data = "helloworld";
        conn.create().creatingParentContainersIfNeeded()
                .forPath("/my/path", data.getBytes(StandardCharsets.UTF_8));
        byte[] bb = conn.getData().forPath("/my/path");
        System.out.println(new String(bb));
        conn.delete().forPath("/my/path");
        System.out.println(conn.checkExists().forPath("/my/path") !=null);
    }

    @Test
    public void test2() throws Exception {
        CuratorFramework conn = CuratorFrameworkFactory.newClient("localhost:2181",new ExponentialBackoffRetry(1000,3));
        conn.start();
        String data = "helloworld";
        conn.create().forPath("/my/path", data.getBytes(StandardCharsets.UTF_8));
        byte[] bb = conn.getData().forPath("/my/path");
        System.out.println(new String(bb));
        conn.delete().forPath("/my/path");
        System.out.println(conn.checkExists().forPath("/my/path") !=null);
    }
}
