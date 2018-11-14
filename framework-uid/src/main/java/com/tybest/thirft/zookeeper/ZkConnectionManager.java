package com.tybest.thirft.zookeeper;

import com.tybest.thirft.config.ZookeeperConfig;
import com.tybest.thirft.zookeeper.callback.DefaultWatcherCallback;
import com.tybest.thirft.zookeeper.callback.WatcherCallback;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.BoundedExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tb
 * @date 2018/11/14 14:12
 */
@Slf4j
public class ZkConnectionManager {

    private final ZookeeperConfig zookeeperConfig;

    public ZkConnectionManager(ZookeeperConfig zookeeperConfig) {
        this.zookeeperConfig = zookeeperConfig;
    }

    public CuratorFramework newClient() {
        return newClient(null);
    }

    public CuratorFramework newClient(ZkAuthInfo authInfo) {
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();
        builder.connectString(getConnectString())
                .connectionTimeoutMs(zookeeperConfig.getConnectionTimeoutMs())
                .sessionTimeoutMs(zookeeperConfig.getSessionTimeoutMs())
                .retryPolicy(new LeafBoundedExponentialBackoffRetry(zookeeperConfig.getRetryIntervalMs(),zookeeperConfig.getRetryIntervalceilingMs(),zookeeperConfig.getRetryTimes()));
        if(null != authInfo && !StringUtils.isEmpty(authInfo.getScheme()) && null != authInfo.getPayload()) {
            builder = builder.authorization(authInfo.getScheme(),authInfo.getPayload());
        }
        CuratorFramework cf = builder.build();
        addListener(cf,new DefaultWatcherCallback());
        cf.start();
        return cf;
    }


    private void addListener(CuratorFramework cf, WatcherCallback callback){
        cf.getCuratorListenable().addListener((curatorFramework, event) -> {
            WatchedEvent we = event.getWatchedEvent();
            if(null != we) {
                callback.execute(event.getStat(),event.getType(),event.getPath());
            }
//            switch (event.getType()) {
//                case WATCHED:
//                    WatchedEvent we = event.getWatchedEvent();
//                    if(we != null){
//                        callback.execute(event.getStat(),event.getType(),event.getPath());
//                    }
//                    break;
//                default:
//                    break;
//            }
        });
        cf.getUnhandledErrorListenable().addListener((s, throwable) -> {
            log.error("error message: {}",s,throwable);
        });
    }


    private String getConnectString() {
        List<String> servers = zookeeperConfig.getServers();
        List<String> conns = new ArrayList<>();
        servers.forEach(s -> conns.add(s+":"+zookeeperConfig.getPort()));
        return StringUtils.join(conns,",");
    }


    /**
     * 重试策略
     */
    class LeafBoundedExponentialBackoffRetry extends BoundedExponentialBackoffRetry {

        LeafBoundedExponentialBackoffRetry(int baseSleepTimeMs, int maxSleepTimeMs, int maxRetries) {
            super(baseSleepTimeMs, maxSleepTimeMs, maxRetries);
        }

        @Override
        protected long getSleepTimeMs(int retryCount, long elapsedTimeMs) {
            return super.getSleepTimeMs(retryCount, elapsedTimeMs);
        }
    }
}
