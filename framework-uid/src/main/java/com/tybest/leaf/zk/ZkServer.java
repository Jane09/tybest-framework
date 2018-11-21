package com.tybest.leaf.zk;

import com.tybest.leaf.config.LeafConfig;
import com.tybest.leaf.zk.auth.AuthInfo;
import com.tybest.leaf.zk.callback.DefaultWatcherCallback;
import com.tybest.leaf.zk.callback.WatcherCallback;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.zookeeper.WatchedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * @author tb
 * @date 2018/11/21 16:47
 */
@Component
@RequiredArgsConstructor
@Getter
@Slf4j
public class ZkServer {

    private final LeafConfig leafConfig;

    private CuratorFramework conn;

    /**
     * 启动服务
     * @param authInfo
     */
    public void start(AuthInfo authInfo) {
        if(conn == null){
            CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();
            builder.connectString(getConnectString())
                    .connectionTimeoutMs(leafConfig.getZk().getConnectionTimeoutMs())
                    .sessionTimeoutMs(leafConfig.getZk().getSessionTimeoutMs())
                    .retryPolicy(new LeafBoundedExponentialBackoffRetry(leafConfig.getZk().getRetryIntervalMs(),leafConfig.getZk().getRetryIntervalceilingMs(),leafConfig.getZk().getRetryTimes()));
            if(null != authInfo && !StringUtils.isEmpty(authInfo.getScheme()) && null != authInfo.getPayload()) {
                builder = builder.authorization(authInfo.getScheme(),authInfo.getPayload());
            }
            conn = builder.build();
            addListener(conn,new DefaultWatcherCallback());
            conn.start();
        }
    }


    public CuratorFramework getConn() {
        return this.conn;
    }


    /**
     * 关闭
     */
    public void close() {
        if(this.conn != null){
            this.conn.close();
            this.conn = null;
        }
    }



    private void addListener(CuratorFramework cf, WatcherCallback callback){
        cf.getCuratorListenable().addListener((curatorFramework, event) -> {
            WatchedEvent we = event.getWatchedEvent();
            if(null != we) {
                callback.execute(event.getStat(),event.getType(),event.getPath());
            }
        });
        cf.getUnhandledErrorListenable().addListener((s, throwable) -> {
            log.error("error message: {}",s,throwable);
        });
    }

    /**
     * 连接示例： ip:port,ip:port
     *           ip:port,ip:port/root 添加一个根目录
     * @return  zk连接池
     */
    private String getConnectString() {
        List<String> servers = leafConfig.getZk().getServers();
        List<String> conns = new ArrayList<>();
        servers.forEach(s -> conns.add(s+":"+leafConfig.getPort()));
        return StringUtils.join(conns,",")+ leafConfig.getZk().getRoot();
    }
}
