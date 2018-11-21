package com.tybest.leaf.rpc;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;

/**
 * 通过RPC访问远程机器
 * @author tb
 * @date 2018/11/21 16:33
 */
public final class LeafClient {


    /**
     * 通过RPC获取远程机器的当前时间
     * @param host
     * @param port
     * @return
     */
    public static long getTimestamp(String host, int port) {
        TSocket transport = new TSocket(host, port);
        TBinaryProtocol protocol = new TBinaryProtocol(transport);
        RemoteHubService.Client client = new RemoteHubService.Client(protocol);
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
