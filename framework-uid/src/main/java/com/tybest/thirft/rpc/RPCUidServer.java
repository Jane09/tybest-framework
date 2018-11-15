package com.tybest.thirft.rpc;

import com.tybest.thirft.config.RpcServerConfig;
import com.tybest.thirft.service.UidService;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportFactory;
import org.springframework.stereotype.Component;

/**
 * 提供给其他UID服务器访问的功能
 * @author tb
 * @date 2018/11/14 11:51
 */
@Component
@Slf4j
public class RpcUidServer {

    private final RpcServerConfig rpcServerConfig;
    private final UidService uidService;
    private TBinaryProtocol.Factory protocolFactory;
    private TTransportFactory transportFactory;

    public RpcUidServer(RpcServerConfig rpcServerConfig, UidService uidService) {
        this.rpcServerConfig = rpcServerConfig;
        this.uidService = uidService;
        protocolFactory = new TBinaryProtocol.Factory();
        transportFactory = new TTransportFactory();
    }


    /**
     * 启动RPC访问
     */
    public void start() {
        RpcUidService.Processor processor = new RpcUidService.Processor<RpcUidService.Iface>( uidService );
        try {
            TServerTransport transport = new TServerSocket(rpcServerConfig.getPort());
            TThreadPoolServer.Args tArgs = new TThreadPoolServer.Args(transport);
            tArgs.processor(processor);
            tArgs.protocolFactory(protocolFactory);
            tArgs.transportFactory(transportFactory);
            tArgs.minWorkerThreads(rpcServerConfig.getMinWorkerThreads());
            tArgs.maxWorkerThreads(rpcServerConfig.getMaxWorkerThreads());
            TServer server = new TThreadPoolServer(tArgs);
            log.info("thrift服务启动成功, 端口={}", rpcServerConfig.getPort());
            server.serve();
        } catch (Exception e) {
            log.error("thrift服务启动失败", e);
        }
    }
}
