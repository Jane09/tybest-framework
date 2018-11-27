package com.tybest.leaf;

import com.tybest.leaf.utils.NetUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

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
    public void testSequential() throws Exception {
        CuratorFramework conn = CuratorFrameworkFactory.builder()
                .connectString("localhost:2181")
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .sessionTimeoutMs(50000)
                .connectionTimeoutMs(3000)
                .build();
        conn.start();
        delCascade(conn,"/root");
        conn.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath("/root/a:1", NetUtils.intToBytes(1));
        conn.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath("/root/b:2", NetUtils.intToBytes(2));
        conn.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath("/root/c:3", NetUtils.intToBytes(3));
        conn.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath("/root/d:4", NetUtils.intToBytes(4));
        conn.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath("/root/e:5", NetUtils.intToBytes(5));
        List<String> children = conn.getChildren().forPath("/root");
        children.forEach(s -> {
            String path = "/root/"+s;
            try {
                System.out.println(NetUtils.bytesToint(conn.getData().forPath(path)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void testPersistent() throws Exception {
        CuratorFramework conn = CuratorFrameworkFactory.builder()
                .connectString("localhost:2181")
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .sessionTimeoutMs(50000)
                .connectionTimeoutMs(3000)
                .build();
        conn.start();
        List<String> children = conn.getChildren().forPath("/leaf-ephemeral");
        children.forEach(s -> {
            String path = "/leaf-ephemeral/"+s;
            try {
                System.out.println(NetUtils.bytesToint(conn.getData().forPath(path)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private boolean isNode(CuratorFramework conn, String path) throws Exception {
        Stat stat = conn.checkExists().forPath(path);
        return stat != null;
    }

    private void delCascade(CuratorFramework conn,String root) throws Exception {
        if(!isNode(conn,root)){
            return;
        }
        List<String> children = conn.getChildren().forPath(root);
        children.forEach(s -> {
            try {
                conn.delete().forPath(root+"/"+s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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
