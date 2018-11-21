package com.tybest.leaf.zk.callback;

import org.apache.curator.framework.api.CuratorEventType;
import org.apache.zookeeper.data.Stat;

public interface WatcherCallback {
    void execute(Stat stat, CuratorEventType type, String path);
}
