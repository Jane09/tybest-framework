package com.tybest.thirft.zookeeper;

import com.tybest.thirft.config.ZookeeperConfig;
import com.tybest.thirft.zookeeper.callback.DefaultWatcherCallback;
import com.tybest.thirft.zookeeper.callback.WatcherCallback;
import com.tybest.thirft.zookeeper.utils.PathUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tb
 * @date 2018/11/14 14:12
 */
@Slf4j
@Component
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
        List<String> servers = zookeeperConfig.getServers();
        List<String> conns = new ArrayList<>();
        servers.forEach(s -> conns.add(s+":"+zookeeperConfig.getPort()));
        return StringUtils.join(conns,",")+ PathUtils.normalize(zookeeperConfig.getRoot());
    }


    public void mkdirs(CuratorFramework cf, String path) throws Exception {
        String npath = PathUtils.parentPath(path);
        if(PathUtils.SEPERATOR.equals(npath)){
            return;
        }
        if(existNode(cf,path,false)) {
           return;
        }
        mkdirs(cf,PathUtils.parentPath(npath));
        log.info("create node : {}",createNode(cf,path,barr((byte)7)));
    }


    private boolean existNode(CuratorFramework cf, String path, boolean watch) throws Exception {
        Stat stat;
        if(watch) {
            stat = cf.checkExists().watched().forPath(PathUtils.normalize(path));
        }else {
            stat = cf.checkExists().forPath(PathUtils.normalize(path));
        }
        return null != stat;
    }

    private String createNode(CuratorFramework cf,String path, byte[] data) throws Exception {
        return createNode(cf,path,data,CreateMode.PERSISTENT);
    }

    private String createNode(CuratorFramework cf, String path, byte[] data, CreateMode mode) throws Exception {
        String npath = PathUtils.normalize(path);
        return cf.create().withMode(mode).withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE).forPath(npath,data);
    }

    private void removeNode(CuratorFramework cf, String path) throws Exception {
        cf.delete().forPath(PathUtils.normalize(path));
    }

    private Stat setData(CuratorFramework cf, String path, byte[] data) throws Exception {
        return cf.setData().forPath(PathUtils.normalize(path),data);
    }

    private byte[] getData(CuratorFramework cf, String path, boolean watch ) throws Exception {
        String npath = PathUtils.normalize(path);
        if(existNode(cf,path,watch)) {
            if(watch) {
                return cf.getData().watched().forPath(npath);
            }else {
                return cf.getData().forPath(npath);
            }
        }
        return null;
    }

    private List<String> getChildren(CuratorFramework cf, String path, boolean watch) throws Exception {
        String npath = PathUtils.normalize(path);
        if(watch) {
            return cf.getChildren().watched().forPath(npath);
        }
        return cf.getChildren().forPath(npath);
    }

    private byte[] barr(byte v) {
        byte[] bs = new byte[1];
        bs[0] = v;
        return bs;
    }
}
