package com.tybest.crawler.event;

import com.tybest.crawler.config.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author tb
 * @date 2018/11/27 11:50
 */
public class EventManager {

    private static final Map<EventType, List<Consumer<Config>>> EVENT_MAP = new ConcurrentHashMap<>();


    /**
     * 注册事件
     * @param eventType
     * @param consumer
     */
    public static void registerEvent(EventType eventType, Consumer<Config> consumer) {
        if(!EVENT_MAP.containsKey(eventType)){
            EVENT_MAP.put(eventType,new ArrayList<>());
        }
        EVENT_MAP.get(eventType).add(consumer);
    }

    /**
     * 触发事件
     * @param eventType
     * @param config
     */
    public static void fireEvent(EventType eventType, Config config) {
        Optional.ofNullable(EVENT_MAP.get(eventType)).ifPresent(consumers -> consumers.forEach(consumer -> consumer.accept(config)));
    }

}
