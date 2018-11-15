package com.tybest.thirft.rpc;

import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 访问其他节点，获取机器时间
 * @author tb
 * @date 2018/11/14 11:51
 */
@Slf4j
public final class RpcUidClient {


    public static Map<String,Long> getTimestampsOfServers(List<String> servers) {
        Map<String,Long> result = new HashMap<>();
        servers.forEach(server -> {
            String[] ss = server.split(":");
            long timestamp = getTimestamp(ss[0],Integer.parseInt(ss[1]));
            result.put(server,timestamp);
        });
        return result;
    }

    /**
     * 获取远程的时间戳
     * @param host  ip地址
     * @param port  端口号
     * @return  返回远程机器时间
     */
    public static long getTimestamp(String host, int port) {
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


    public static long getTimestamp(String ip, int port ,int timeout) throws TException {
        TTransport transport = new TSocket(ip,port,timeout);
        TProtocol protocol = new TBinaryProtocol(transport);
        RpcUidService.Client client = new RpcUidService.Client(protocol);
        transport.open();
        long timestamp = client.getTimestamp(System.currentTimeMillis());
        log.info("TimeStamp: {}", timestamp);
        transport.close();
        return timestamp;
    }
}
