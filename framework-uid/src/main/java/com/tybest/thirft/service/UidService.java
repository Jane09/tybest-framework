package com.tybest.thirft.service;

import com.tybest.thirft.rpc.RpcUidService;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

/**
 * @author tb
 * @date 2018/11/14 13:40
 */
@Service
public class UidService implements RpcUidService.Iface {


    @Override
    public long getTimestampe(long myTimestamp) throws TException {
        return System.currentTimeMillis();
    }

    @Override
    public String getUid(String param) throws TException {
        //TODO genterate snowflake uuid
        return null;
    }
}
