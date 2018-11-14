package com.tybest.thirft.rpc;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;
import org.springframework.stereotype.Component;

/**
 * 访问其他节点，获取机器时间
 * @author tb
 * @date 2018/11/14 11:51
 */
@Component
public class RpcUidClient {


    /**
     * 获取远程的时间戳
     * @param host  ip地址
     * @param port  端口号
     * @return  返回远程机器时间
     */
    public long getRemoteTimestamp(String host, int port) {
        TSocket transport = new TSocket(host, port);
        TBinaryProtocol protocol = new TBinaryProtocol(transport);
        RpcUidService.Client client = new RpcUidService.Client(protocol);
        try {
            transport.open();
            return client.getTimestamp(System.currentTimeMillis());
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            transport.close();
        }
        return 0L;
    }
}
