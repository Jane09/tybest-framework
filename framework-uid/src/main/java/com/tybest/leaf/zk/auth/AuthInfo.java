package com.tybest.leaf.zk.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * zookeeper 验证信息
 * @author tb
 * @date 2018/11/14 14:10
 */
@AllArgsConstructor
@Getter
public class AuthInfo implements Serializable {
    public String scheme;
    public byte[] payload = null;
}
