package com.tybest.seckill.config;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author tb
 * @date 2018/12/3 11:17
 */
@ServerEndpoint("/websocket/{userId}")
@Component
@Slf4j
@EqualsAndHashCode
public class WebSocketServer {

    /**
     * 当前在线人数
     */
    private static volatile LongAdder onlines = new LongAdder();

    /**
     * 当前连接集合
     */
    private static CopyOnWriteArraySet<WebSocketServer> servers = new CopyOnWriteArraySet<>();

    private Session session;

    private String userId;


    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        servers.add(this);
        this.session = session;
        this.userId = userId;
        onlines.increment();
        sendMessage("连接成功");
        log.info("用户={}上线",userId);
    }

    @OnClose
    public void onClose(){
        servers.remove(this);
        onlines.decrement();
        log.info("用户={}离线",this.userId);
    }


    @OnMessage
    public void onMessage(String message) {
        log.info("收到来自={}的消息={}",this.userId,message);
        //群发
        servers.forEach(server -> server.sendMessage(message));
    }

    @OnError
    public void onError(Session session,Throwable error) {
        log.error("websocket occur error",error);
    }


    /**
     * 发送
     * @param message
     * @param userId
     */
    public void sendInfo(String message, String userId) {
        if(StringUtils.isBlank(userId)){
            servers.forEach(s -> s.sendMessage(message));
        }
        servers.forEach(s -> {
            if(s.userId.equals(userId)) {
                s.sendMessage(message);
            }
        });
    }

    public synchronized int getOnlines(){
        return onlines.intValue();
    }


    private void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("发送消息失败,用户ID={}，内容={}",this.userId,message,e);
        }
    }
}
