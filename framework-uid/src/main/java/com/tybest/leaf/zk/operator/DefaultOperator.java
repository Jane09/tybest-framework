
package com.tybest.leaf.zk.operator;

import com.tybest.leaf.zk.ZkOperator;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

/**
 * @author tb
 * @date 2018/11/21 17:25
 */
public class DefaultOperator implements ZkOperator {

    private static final DefaultOperator OPERATOR = new DefaultOperator();
    private DefaultOperator(){}

    public static ZkOperator getInstance(){
        return OPERATOR;
    }

    @Override
    public String addNode(CuratorFramework conn, String path, byte[] data,CreateMode mode) throws Exception {
        return addNode(conn,path,data, mode);
    }


}
