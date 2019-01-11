package com.tybest.leaf.zk;

import com.tybest.leaf.exception.ZkException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * @author tybest
 */
public interface ZkOperator {

    /**
     * 判定是否存在
     * @param conn
     * @param path
     * @param watched
     * @return
     * @throws Exception
     */
    default boolean exists(CuratorFramework conn, String path, boolean watched) throws Exception {
            Stat stat;
            if(watched) {
                stat = conn.checkExists().watched().forPath(path);
            }else {
                stat = conn.checkExists().forPath(path);
            }
            return null != stat;
    }

    /**
     * 添加zk节点
     * @param conn
     * @param path
     * @param data
     * @param mode
     * @return
     * @throws Exception
     */
    default String addNode(CuratorFramework conn, String path, byte[] data, CreateMode mode) throws Exception {
        if(exists(conn,path,false)) {
            return path;
        }
        return conn.create().withMode(mode).withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE).forPath(path,data);
    }

    /**
     * 删除节点
     * @param conn
     * @param path
     * @throws Exception
     */
    default void delNode(CuratorFramework conn, String path) throws Exception {
        conn.delete().forPath(path);
    }

    /**
     * 编辑节点
     * @param conn
     * @param path
     * @param data
     * @return
     * @throws Exception
     */
    default Stat editNode(CuratorFramework conn, String path, byte[] data) throws Exception {
        if(!exists(conn,path,false)){
           throw new ZkException("Node = "+path+" Not Found");
        }
        return conn.setData().forPath(path,data);
    }

    default List<String> getChildren(CuratorFramework conn, String path, boolean watched) throws Exception {
        if(!exists(conn,path,watched)){
            return null;
        }
        if(watched) {
            return conn.getChildren().watched().forPath(path);
        }
        return conn.getChildren().forPath(path);
    }

    /**
     * 获取节点数据
     * @param conn
     * @param path
     * @param watched
     * @return
     * @throws Exception
     */
    default byte[] getData(CuratorFramework conn, String path, boolean watched) throws Exception {
        if(!exists(conn,path,watched)) {
            return null;
        }
        if(watched) {
            return conn.getData().watched().forPath(path);
        }else {
            return conn.getData().forPath(path);
        }
    }
}
