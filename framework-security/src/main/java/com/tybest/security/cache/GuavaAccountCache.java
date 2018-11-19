package com.tybest.security.cache;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author tb
 * @date 2018/11/19 13:40
 */
public class GuavaAccountCache extends TAccountCache {


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
