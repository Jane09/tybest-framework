package com.tybest.leaf.zk;

import com.tybest.base.utils.DateUtils;
import com.tybest.leaf.config.LeafConfig;
import com.tybest.leaf.exception.ZkException;
import com.tybest.leaf.rpc.LeafClient;
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
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigInteger;
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
    private static final String SICILON = ":";

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
     */
    public void start(AuthInfo authInfo) {
        if(null != conn){
            close();
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
            //校验周期性上传的时间
            if(!checkCycleUploadTimestamp()){
                throw new ZkException("The current machine time had been changed");
            }
            //校验平均时间
            if(!isAverageRange()){
                throw new ZkException("Current server timestamp is not illegal");
            }
            long machineId = getWorkId(getPath());
            log.info("obtain a machineId = {}", machineId);
            //启动临时节点
            startTimer(machineId);
            log.info("Next uid = {}",this.snowflakeUtils.nextId());
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
        String ppath = DefaultOperator.getInstance().addNode(conn,this.leafConfig.getZk().getPersistent(),EMPTY_DATA,CreateMode.PERSISTENT);
        log.info("create persistent node for path = {}",ppath);

        String epath = DefaultOperator.getInstance().addNode(conn,this.leafConfig.getZk().getEphemeral(),EMPTY_DATA,CreateMode.PERSISTENT);
        log.info("create ephemeral node for path = {}",epath);
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

    private boolean checkCycleUploadTimestamp() throws Exception {
        String rpath = getCompleteEphemeralPath(getPath());
        byte[] timestamp = DefaultOperator.getInstance().getData(conn,rpath,false);
        if(timestamp == null){
            return true;
        }
        long last = NetUtils.bytesToLong(timestamp);
        if(last > System.currentTimeMillis()) {
            log.error("Cycle Upload Timestamp greater than the now machine time ");
            return false;
        }
        return true;
    }

    private boolean isAverageRange() throws Exception {
        String ip = NetUtils.getInternetIp();
        List<String> children = DefaultOperator.getInstance().getChildren(conn,this.leafConfig.getZk().getEphemeral(),false);
        BigInteger avg = new BigInteger("0");
        if(children != null){
            int size = children.size();
           for(String server: children){
                String[] ss = server.split(SICILON);
                long timestamp = LeafClient.getTimestamp(ss[0],Integer.parseInt(ss[1]));
                log.info("ip={} port={} timestamp={}",ss[0],ss[1],timestamp);
                if(timestamp > 0 && !ip.equals(ss[0])) {
                    avg = avg.add(BigInteger.valueOf(timestamp));
                }
            }
            if(avg.compareTo(BigInteger.ZERO) == 0){
                return true;
            }
            if(size ==1){
                return true;
            }
            avg = avg.divide(BigInteger.valueOf(size-1));
            if(Math.abs(avg.longValue() - System.currentTimeMillis()) > this.leafConfig.getZk().getAverageTimestampThreshold()) {
                log.error("Current server timestamp :{} does not match the average timestamp: {}",DateUtils.format(System.currentTimeMillis(),DateUtils.DEF_TIME),
                        DateUtils.format(avg.longValue(),DateUtils.DEF_TIME));
                return false;
            }
        }
        return true;
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
            return NetUtils.bytesToLong(DefaultOperator.getInstance().getData(conn,rpath,false));
        }
        long workerId = 1L;
        InterProcessMutex mutex = new InterProcessMutex(conn,path);
        try{
            mutex.acquire(1, TimeUnit.SECONDS);
            String npath = DefaultOperator.getInstance().addNode(conn,rpath,NetUtils.longToBytes(workerId),CreateMode.PERSISTENT_SEQUENTIAL);
            //0000000051
            workerId = Long.parseLong(npath.replace(rpath,""));
        }catch (Exception ex){
            throw new ZkException(ex);
        }finally {
            mutex.release();
        }

        return workerId;
    }

    private void startTimer(long machineId) throws Exception {
        snowflakeUtils = new SnowflakeUtils(this.leafConfig.getZk().getDatacenter(),machineId);
        String rpath = getCompleteEphemeralPath(getPath());
        boolean exist = DefaultOperator.getInstance().exists(conn,rpath,false);
        if(!exist){
            DefaultOperator.getInstance().addNode(conn,rpath,NetUtils.longToBytes(System.currentTimeMillis()),CreateMode.EPHEMERAL);
        }
        //开启定时上传当前机器时间戳
        executor.scheduleAtFixedRate(() -> {
            try {
                byte[] timestamp = DefaultOperator.getInstance().getData(conn,rpath,false);
                if(timestamp != null){
                    log.info("上次上报的时间：{}", DateUtils.format(NetUtils.bytesToLong(timestamp),DateUtils.DEF_TIME));
                }
                log.info("report timpstamp to {}",rpath);
                DefaultOperator.getInstance().editNode(conn,rpath,NetUtils.longToBytes(System.currentTimeMillis()));
            } catch (Exception e) {
                log.error("Report Timer Error",e);
            }
        },3L,3L,TimeUnit.SECONDS);
    }


    /**
     * 关闭
     */
    private void close() {
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
        cf.getUnhandledErrorListenable().addListener((s, throwable) -> log.error("error message: {}",s,throwable));
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
