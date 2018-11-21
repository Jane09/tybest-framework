package com.tybest.leaf.zk.callback;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.zookeeper.data.Stat;

/**
 * @author tb
 * @date 2018/11/14 15:04
 */
@Slf4j
public class DefaultWatcherCallback implements WatcherCallback {
    @Override
    public void execute(Stat stat, CuratorEventType type, String path) {
        log.info(path);
    }
}
