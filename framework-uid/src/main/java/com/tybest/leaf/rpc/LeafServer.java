package com.tybest.leaf.rpc;

import com.tybest.leaf.config.LeafConfig;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportFactory;
import org.springframework.stereotype.Component;

/**
 * @author tb
 * @date 2018/11/21 16:34
 */
@Getter
@RequiredArgsConstructor
@Slf4j
@Component
public class LeafServer {
    private final LeafConfig leafConfig;
    private final LeafService leafService;


    public void start() {
        TBinaryProtocol.Factory protocolFactory = new TBinaryProtocol.Factory();
        TTransportFactory transportFactory = new TTransportFactory();
        RemoteHubService.Processor processor = new RemoteHubService.Processor<RemoteHubService.Iface>( leafService );
        try {
            TServerTransport transport = new TServerSocket(leafConfig.getPort());
            TThreadPoolServer.Args tArgs = new TThreadPoolServer.Args(transport);
            tArgs.processor(processor);
            tArgs.protocolFactory(protocolFactory);
            tArgs.transportFactory(transportFactory);
            tArgs.minWorkerThreads(leafConfig.getMinWorkerThreads());
            tArgs.maxWorkerThreads(leafConfig.getMaxWorkerThreads());
            TServer server = new TThreadPoolServer(tArgs);
            log.info("thrift服务启动成功, 端口={}", leafConfig.getPort());
            server.serve();
        } catch (Exception e) {
            log.error("thrift服务启动失败", e);
        }
    }
}
