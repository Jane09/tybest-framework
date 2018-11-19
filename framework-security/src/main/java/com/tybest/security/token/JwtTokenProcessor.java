package com.tybest.security.token;

import com.tybest.security.config.metadata.JwtTokenConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tb
 * @date 2018/11/19 13:50
 */
@Slf4j
public class JwtTokenProcessor implements TokenProcessor {

    private final JwtTokenConfig jwtTokenConfig;

    public JwtTokenProcessor(JwtTokenConfig jwtTokenConfig) {
        this.jwtTokenConfig = jwtTokenConfig;
    }

    @Override
    public String generate(TokenData data, TokenType tokenType) {
        return null;
    }

    @Override
    public TokenData retrieve(String token, TokenType tokenType) {
        return null;
    }

    @Override
    public boolean needRefresh(String token) {
        return false;
    }
}
