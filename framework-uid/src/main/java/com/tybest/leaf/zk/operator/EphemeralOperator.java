
package com.tybest.leaf.zk.operator;

import com.tybest.leaf.zk.ZkOperator;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

/**
 * @author tb
 * @date 2018/11/21 17:25
 */
public class EphemeralOperator implements ZkOperator {

    private static final EphemeralOperator OPERATOR = new EphemeralOperator();
    private EphemeralOperator(){}

    public static ZkOperator getInstance(){
        return OPERATOR;
    }

    @Override
    public String addNode(CuratorFramework conn, String path, byte[] data) throws Exception {
        return addNode(conn,path,data, CreateMode.EPHEMERAL);
    }
}
