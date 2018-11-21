package com.tybest.leaf.zk.operator;

import com.tybest.leaf.zk.ZkOperator;
import org.apache.curator.framework.CuratorFramework;

/**
 * @author tb
 * @date 2018/11/21 17:40
 */
public class EphemeralSequentialOperator implements ZkOperator {

    @Override
    public String addNode(CuratorFramework conn, String path, byte[] data) throws Exception {
        return null;
    }
}
