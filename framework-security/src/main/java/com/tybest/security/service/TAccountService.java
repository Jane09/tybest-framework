package com.tybest.security.service;

import com.tybest.security.exception.TAccountNotFoundException;
import com.tybest.security.model.TAccount;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 提供接口集成，查询账户信息
 * @author tb
 */
public interface TAccountService extends UserDetailsService {

    TAccount loadTAccountByUsername(String username) throws TAccountNotFoundException;

    /**
     * 加载用户名
     * @param username  登录名
     * @return  返回
     * @throws UsernameNotFoundException 用户名不存在
     */
    @Override
    default UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return loadTAccountByUsername(username);
        } catch (TAccountNotFoundException e) {
            throw new UsernameNotFoundException("", e);
        }
    }
}
