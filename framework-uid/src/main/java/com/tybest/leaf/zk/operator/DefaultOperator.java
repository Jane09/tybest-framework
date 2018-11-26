
package com.tybest.leaf.zk.operator;

import com.tybest.leaf.zk.ZkOperator;
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
}
