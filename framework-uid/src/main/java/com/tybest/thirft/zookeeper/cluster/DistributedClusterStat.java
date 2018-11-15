package com.tybest.thirft.zookeeper.cluster;

import com.tybest.thirft.config.ZookeeperConfig;
import com.tybest.thirft.zookeeper.ZkConnectionManager;
import com.tybest.thirft.zookeeper.callback.WatcherCallback;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author tb
 * @date 2018/11/15 11:23
 */
@Component
public class DistributedClusterStat implements ClusterStat {

    private final ZkConnectionManager zkConnectionManager;
    private final ZookeeperConfig zookeeperConfig;

    private AtomicBoolean active;
    private CuratorFramework zk;
    private WatcherCallback watcherCallback;

    public DistributedClusterStat(ZkConnectionManager zkConnectionManager, ZookeeperConfig zookeeperConfig) {
        this.zkConnectionManager = zkConnectionManager;
        this.zookeeperConfig = zookeeperConfig;

        this.zk = zkConnectionManager.newClient();
        try{
            zkConnectionManager.mkdirs(this.zk,zookeeperConfig.getRoot());
            this.zk.close();
            active = new AtomicBoolean(true);
            watcherCallback = (stat, type, path) -> {
                if(active.get()) {

                }
            };
        }catch (Exception ex) {

        }

    }



}
