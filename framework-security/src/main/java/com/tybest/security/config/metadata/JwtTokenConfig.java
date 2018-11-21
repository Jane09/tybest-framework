package com.tybest.security.config.metadata;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

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

    private String tokenIssuer;


    private String signAlgorithm;


    public SignatureAlgorithm getSignatureAlgorithm(){
        if(StringUtils.isBlank(this.signAlgorithm)){
            return SignatureAlgorithm.NONE;
        }
        return SignatureAlgorithm.forName(this.signAlgorithm);
    }

}
