package com.tybest.thirft.service;

import com.tybest.thirft.rpc.RpcUidService;
import org.springframework.stereotype.Service;

/**
 * @author tb
 * @date 2018/11/14 13:40
 */
@Service
public class UidService implements RpcUidService.Iface {


    @Override
    public long getTimestamp(long myTimestamp) {
        return System.currentTimeMillis();
    }

    @Override
    public String getUid(String param) {
        //TODO genterate snowflake uuid
        return null;
    }
}
