package com.tybest.security.cache;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author tb
 * @date 2018/11/19 13:41
 */
public class RedisAccountCache extends TAccountCache {
    @Override
    public UserDetails getUserFromCache(String s) {
        return null;
    }

    @Override
    public void putUserInCache(UserDetails userDetails) {

    }

    @Override
    public void removeUserFromCache(String s) {

    }
}
