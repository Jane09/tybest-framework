package com.tybest.thirft.server;

import com.tybest.base.utils.DateUtils;
import com.tybest.base.utils.NetUtils;
import com.tybest.thirft.config.RpcServerConfig;
import com.tybest.thirft.config.ZookeeperConfig;
import com.tybest.thirft.rpc.RpcUidClient;
import com.tybest.thirft.zookeeper.ZkConnectionManager;
import com.tybest.thirft.zookeeper.cluster.DistributedClusterStat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 提供
 * @author tb
 * @date 2018/11/14 14:09
 */
@Component
@Slf4j
public class UidServer {


    private AtomicBoolean active = new AtomicBoolean(false);
    private final DistributedClusterStat clusterStat;
    private final ZkConnectionManager zkConnectionManager;
    private final ZookeeperConfig zookeeperConfig;
    private final RpcServerConfig rpcServerConfig;
    public UidServer(DistributedClusterStat clusterStat, ZkConnectionManager zkConnectionManager, ZookeeperConfig zookeeperConfig, RpcServerConfig rpcServerConfig){
        this.clusterStat = clusterStat;
        this.zkConnectionManager = zkConnectionManager;
        this.zookeeperConfig = zookeeperConfig;
        this.rpcServerConfig = rpcServerConfig;
    }

    public void start() {
        try{
            zkConnectionManager.mkdirs(clusterStat.getZk(),zookeeperConfig.getForever());
            zkConnectionManager.mkdirs(clusterStat.getZk(),zookeeperConfig.getEphemeral());
            if(isLegal()) {
                stop();
            }
        }catch (Exception ex) {

        }

    }





    public void stop() {
        active.set(false);
        clusterStat.close();
    }


    public long getUid() {
        return 0L;
    }


    /**
     * 判断当前机器时间是否合法
     * @return  返回判断结果
     * @throws Exception
     */
    private boolean isLegal() throws Exception {
        List<String> servers =zkConnectionManager.getChildren(clusterStat.getZk(),zookeeperConfig.getEphemeral(),false);
        Map<String,Long> timestamps = RpcUidClient.getTimestampsOfServers(servers);
        timestamps.remove(NetUtils.getInternetIp()+":"+rpcServerConfig.getPort());
        if(timestamps.isEmpty()) {
            return true;
        }
        BigInteger total = BigInteger.ZERO;
        Set<Map.Entry<String,Long>> entries = timestamps.entrySet();
        for(Map.Entry<String,Long> entry: entries) {
            total = total.add(BigInteger.valueOf(entry.getValue()));
        }
        long average = total.divide(BigInteger.valueOf(timestamps.size())).longValue();
        if(Math.abs(average - System.currentTimeMillis()) > zookeeperConfig.getAverageTimestampThreshold()) {
            log.error("current server timestamp :" + DateUtils.format(new Date(),DateUtils.DEF_TIME)
                    + " does not match the peers average timestamp : "
                    + DateUtils.format(average,DateUtils.DEF_TIME) + " ,exit jvm.");
            return false;
        }
        return true;
    }
}
