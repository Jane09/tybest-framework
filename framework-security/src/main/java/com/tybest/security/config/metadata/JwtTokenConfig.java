package com.tybest.security.config.metadata;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author tb
 * @date 2018/11/16 9:58
 */
@Getter
@Setter
@RequiredArgsConstructor
public class JwtTokenConfig {

    private String signKey;
    /**
     * 过期时间
     */
    private Integer accessExpires;
    /**
     * 刷新时间
     */
    private Integer refreshExpires;
    /**
     * 剩余多少时间进行刷新
     */
    private Integer refreshOnRemains;

}
