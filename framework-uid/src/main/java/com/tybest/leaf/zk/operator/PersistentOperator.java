package com.tybest.leaf.zk.operator;

import com.tybest.leaf.zk.ZkOperator;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

/**
 * @author tb
 * @date 2018/11/21 17:24
 */
public class PersistentOperator implements ZkOperator {

    private static final PersistentOperator OPERATOR = new PersistentOperator();

    private PersistentOperator(){}

    public static ZkOperator getInstance(){
        return OPERATOR;
    }

    @Override
    public String addNode(CuratorFramework conn, String path, byte[] data) throws Exception {
        return addNode(conn,path,data, CreateMode.PERSISTENT);
    }
}
