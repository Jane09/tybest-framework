package com.tybest.leaf.zk;

import com.tybest.leaf.config.LeafConfig;
import com.tybest.leaf.exception.ZkException;
import com.tybest.leaf.rpc.LeafServer;
import com.tybest.leaf.rpc.LeafService;
import com.tybest.leaf.utils.NetUtils;
import com.tybest.leaf.utils.SnowflakeUtils;
import com.tybest.leaf.zk.auth.AuthInfo;
import com.tybest.leaf.zk.callback.DefaultWatcherCallback;
import com.tybest.leaf.zk.callback.WatcherCallback;
import com.tybest.leaf.zk.operator.DefaultOperator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @author tb
 * @date 2018/11/21 16:47
 */
@Component
@RequiredArgsConstructor
@Getter
@Slf4j
public class ZkServer {

    private static final byte[] EMPTY_DATA = new byte[0];
    private static final String FILE_SEPERATEOR = File.separator;
    private static final String SEPERATOR = "/";

    private final LeafServer leafServer;
    private final LeafConfig leafConfig;
    private final LeafService leafService;
    private volatile boolean started = false;
    private final ScheduledExecutorService executor =new ScheduledThreadPoolExecutor(1,new BasicThreadFactory.Builder().namingPattern("leaf-timer-report-timestam-%d").daemon(true).build());
    private CuratorFramework conn;
    private SnowflakeUtils snowflakeUtils;

    /**
     * 使用的是内网IP
     * leaf-persistent
     *    ip:port -> workid
     *    ...
     *leaf-ephermal
     *    ip:port -> timestamp
     *    ...
     *
     *
     * 启动服务
     * @param authInfo
     */
    public void start(AuthInfo authInfo) {
        if(null == conn){
            log.error("启动ZK链接失败");
        }
        try{
            CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();
            builder.connectString(getConnString())
                    .connectionTimeoutMs(leafConfig.getZk().getConnectionTimeoutMs())
                    .sessionTimeoutMs(leafConfig.getZk().getSessionTimeoutMs())
                    .retryPolicy(new LeafBoundedExponentialBackoffRetry(leafConfig.getZk().getRetryIntervalMs(),leafConfig.getZk().getRetryIntervalceilingMs(),leafConfig.getZk().getRetryTimes()));
            if(null != authInfo && !StringUtils.isEmpty(authInfo.getScheme()) && null != authInfo.getPayload()) {
                builder = builder.authorization(authInfo.getScheme(),authInfo.getPayload());
            }
            conn = builder.build();
            addListener(conn,new DefaultWatcherCallback());
            conn.start();
            started = true;
            log.info("start up zk completely");
            //准备环境
            prepare();
            //
        }catch (Exception ex){
            throw new ZkException(ex);
        }
        leafServer.start();
    }
    private CuratorFramework getConn() {
        if(!started){
            start(null);
        }
        return this.conn;
    }

    private void prepare() throws Exception {
        CuratorFramework conn = getConn();
        ZkOperator operator = DefaultOperator.getInstance();
        operator.addNode(conn,this.leafConfig.getZk().getPersistent(),EMPTY_DATA,CreateMode.PERSISTENT_SEQUENTIAL);
        operator.addNode(conn,this.leafConfig.getZk().getEphemeral(),EMPTY_DATA,CreateMode.PERSISTENT);
        long machineId = getWorkId(getPath());
        snowflakeUtils = new SnowflakeUtils(this.leafConfig.getZk().getDatacenter(),machineId);
    }

    private String getPath() {
        return SEPERATOR+NetUtils.getInternetIp()+":"+this.leafConfig.getPort();
    }

    private String getCompletePeristentPath(String subPath) {
        return this.leafConfig.getZk().getPersistent()+subPath;
    }

    private String getCompleteEphemeralPath(String subPath) {
        return this.leafConfig.getZk().getEphemeral()+subPath;
    }

    /**
     * 自动生成workerId
     * @return 返回workerId
     */
    private long getWorkId(String path) throws Exception {
        CuratorFramework conn = getConn();
        String rpath = getCompletePeristentPath(getPath());
        boolean exist = DefaultOperator.getInstance().exists(conn,rpath,false);
        if(exist){
            startTimer();
            return NetUtils.bytesToLong(DefaultOperator.getInstance().getData(conn,rpath,false));
        }

        long workerId = 1L;
        InterProcessMutex mutex = new InterProcessMutex(conn,path);
        try{
            mutex.acquire(1, TimeUnit.SECONDS);
            List<String> children = DefaultOperator.getInstance().getChildren(conn,this.leafConfig.getZk().getPersistent(),false);
            if(children != null){
                workerId= children.size()+1;
            }
            DefaultOperator.getInstance().addNode(conn,rpath,NetUtils.longToBytes(workerId),CreateMode.PERSISTENT_SEQUENTIAL);
        }finally {
            mutex.release();
        }
        startTimer();
        return workerId;
    }

    private void startTimer() throws Exception {
        boolean exist = DefaultOperator.getInstance().exists(conn,getCompleteEphemeralPath(getPath()),false);
        if(!exist){
            DefaultOperator.getInstance().addNode(conn,getCompleteEphemeralPath(getPath()),NetUtils.longToBytes(System.currentTimeMillis()),CreateMode.EPHEMERAL);
        }
        //开启定时上传当前机器时间戳
        executor.scheduleAtFixedRate(() -> {
            try {
                log.info("report timpstamp to {}",getCompleteEphemeralPath(getPath()));
                DefaultOperator.getInstance().editNode(conn,getCompleteEphemeralPath(getPath()),NetUtils.longToBytes(System.currentTimeMillis()));
            } catch (Exception e) {
                log.error("Report Timer Error",e);
            }
        },5L,5L,TimeUnit.SECONDS);
    }


    /**
     * 校验是否已注册
     */
    private void checkRegister(){

    }

    /**
     * 获取所有节点
     */
    private void getEphemeralNodes() {

    }


    private void getWorkId(String ip, String port) {

    }

    /**
     * 校验周期性上传的时间戳
     */
    private void checkCycleUploadTimestamp(){

    }


    private void checkAverageConstrint() {

    }



    /**
     * 关闭
     */
    public void close() {
        if(this.conn != null){
            this.conn.close();
            this.conn = null;
            log.info("close zk completely");
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
    private String getConnString() {
        List<String> servers = leafConfig.getZk().getServers();
        List<String> conns = new ArrayList<>();
        servers.forEach(s -> conns.add(s+":"+leafConfig.getZk().getPort()));
        return StringUtils.join(conns,",");
    }
}
