package com.tybest.thirft.zookeeper.cluster;

import com.tybest.thirft.config.ZookeeperConfig;
import com.tybest.thirft.zookeeper.ZkConnectionManager;
import com.tybest.thirft.zookeeper.callback.WatcherCallback;
import com.tybest.thirft.zookeeper.utils.ProcessUtils;
import lombok.Getter;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Getter
    private CuratorFramework zk;

    private WatcherCallback watcherCallback;

    @Autowired
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
            this.zk = null;
            this.zk = zkConnectionManager.newClient();
            this.zkConnectionManager.addListener(this.zk,watcherCallback);
        }catch (Exception ex) {
            ProcessUtils.exitDelay(-1, ex.getMessage());
        }
    }

    @Override
    public void close() {
        if(null != this.zk) {
            this.zk.close();
            this.zk = null;
        }
        ProcessUtils.sleep(5000);
    }
}
