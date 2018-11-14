package com.tybest.thirft.rpc;

public interface RPCAccess {

    RpcUidService.Client newClient();

    void open();

    void close();
}
