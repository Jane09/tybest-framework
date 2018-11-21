package com.tybest.leaf.rpc;

import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

/**
 * @author tb
 * @date 2018/11/21 16:36
 */
@Service
public class LeafService implements RemoteHubService.Iface {

    //最大允许的偏差
    private static final long MAX_OFFSET = 5000L;

    @Override
    public long getTimestamp(long myTimestamp) throws TException {
        long timestamp = System.currentTimeMillis();
        long offset = Math.abs(timestamp - myTimestamp);
        if(MAX_OFFSET < offset){
            return -1;
        }
        return timestamp;
    }

    @Override
    public String getId(String param) throws TException {
        //TODO 待完善生成ID的方式，提供给当前服务使用
        return null;
    }
}
