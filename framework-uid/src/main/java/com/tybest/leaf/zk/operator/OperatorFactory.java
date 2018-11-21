package com.tybest.leaf.zk.operator;

import com.tybest.leaf.exception.ZkException;
import com.tybest.leaf.zk.ZkOperator;
import org.apache.zookeeper.CreateMode;

/**
 * @author tb
 * @date 2018/11/21 17:35
 */
public final class OperatorFactory {

    private OperatorFactory(){}

    public static ZkOperator getOperator(CreateMode mode){
        switch (mode) {
            case PERSISTENT:
                return PersistentOperator.getInstance();
            case EPHEMERAL:
                return EphemeralOperator.getInstance();
            default:
                throw new ZkException("没有指定的操作");
        }
    }
}
