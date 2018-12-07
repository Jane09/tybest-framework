package com.tybest.tcc.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 事务传播类型
 * @author tb
 * @date 2018/12/7 15:24
 */
@AllArgsConstructor
public enum  Propagation {
    /**
     * 必要
     */
    REQUIRED(0),
    /**
     * 支持
     */
    SUPPORTS(1),
    /**
     * 托管
     */
    MANDATORY(2),
    /**
     * 新启事务
     */
    REQUIRES_NEW(3);

    @Getter
    private int value;
}
