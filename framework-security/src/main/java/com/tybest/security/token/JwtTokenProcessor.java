package com.tybest.security.token;

import com.alibaba.fastjson.JSON;
import com.tybest.security.config.metadata.DefaultJwtTokenConfig;
import com.tybest.security.config.metadata.JwtTokenConfig;
import com.tybest.security.exception.IllegalRawTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author tb
 * @date 2018/11/19 13:50
 */
@Slf4j
public class JwtTokenProcessor implements TokenProcessor {

    private final JwtTokenConfig jwtTokenConfig;
    private static final String TYPE = "type";
    private static final String USER_ID = "userId";
    private static final String CLIENT_ID = "clientId";
    private static final String CLIENT_TYPE = "clientType";
    private static final String CLIENT_VERSION = "clientVersion";

    public static void main(String[] args) {
        TokenProcessor processor = new JwtTokenProcessor();
        TokenData data = new TokenData("1","admin","123456");
        String token = processor.generate(data,TokenType.ACCESS);
        System.out.println(token);
        System.out.println(JSON.toJSONString(processor.retrieve(token,TokenType.ACCESS)));
    }

    public JwtTokenProcessor(){
        this(new DefaultJwtTokenConfig());
    }


    public JwtTokenProcessor(JwtTokenConfig jwtTokenConfig) {
        this.jwtTokenConfig = jwtTokenConfig;
    }

    @Override
    public String generate(TokenData data, TokenType tokenType) {
        final int expires = getExpires(tokenType);
        Map<String,Object> claims = new HashMap<>(16);
        claims.put("type", tokenType.name());
        claims.put("expires", expires);
        claims.put("userId", data.getUserId());
        claims.put("clientId", data.getClientId());
        if (StringUtils.hasText(data.getClientType())) {
            claims.put("clientType", data.getClientType());
        }
        if (StringUtils.hasText(data.getClientVersion())) {
            claims.put("clientVersion", data.getClientVersion());
        }
        claims.put("random", UUID.randomUUID());
        Calendar now = Calendar.getInstance();
        Calendar expire = Calendar.getInstance();
        expire.add(Calendar.SECOND,expires);
        return Jwts.builder().setIssuer(jwtTokenConfig.getTokenIssuer())
                .setIssuedAt(now.getTime())
                .setExpiration(expire.getTime())
                .signWith(jwtTokenConfig.getSignatureAlgorithm(),jwtTokenConfig.getSignKey())
                .setSubject(data.getUsername())
                .addClaims(claims)
                .compact();
    }

    @Override
    public TokenData retrieve(String token, TokenType tokenType) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(jwtTokenConfig.getSignKey()).parseClaimsJws(token);
        final String type = getBodyAttr(claims,TYPE);
        if(!StringUtils.hasText(type) || !tokenType.name().equals(type)) {
            log.error(String.format("Illegal %s raw token data!",tokenType.name()));
            throw new IllegalRawTokenException("Illegal raw token");
        }
        final String userId = getBodyAttr(claims,USER_ID);
        final String username = claims.getBody().getSubject();
        if(!StringUtils.hasText(userId) || !StringUtils.hasText(username)) {
            log.error("Illegal user data of {} raw token", tokenType.name());
            throw new IllegalRawTokenException("Illegal user data!");
        }
        final String clientId = getBodyAttr(claims,CLIENT_ID);
        if(!StringUtils.hasText(clientId)) {
            throw new IllegalRawTokenException("Illegal client data");
        }
        TokenData data = new TokenData(userId,username,clientId);
        final String clientType =getBodyAttr(claims,CLIENT_TYPE);
        final String clientVersion = getBodyAttr(claims,CLIENT_VERSION);
        data.setClientType(clientType);
        data.setClientVersion(clientVersion);
        return data;
    }

    private String getBodyAttr(Jws<Claims> claims,String clazz) {
        return claims.getBody().get(clazz,String.class);
    }

    @Override
    public boolean needRefresh(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(jwtTokenConfig.getSignKey()).parseClaimsJws(token);
        if(!TokenType.REFRESH.name().equals(getBodyAttr(claims,TYPE))) {
            return false;
        }
        long remains = claims.getBody().getExpiration().getTime() - System.currentTimeMillis();
        return remains < jwtTokenConfig.getRefreshOnRemains();
    }



    private int getExpires(TokenType type) {
        int expires = 0;
        if (TokenType.ACCESS.equals(type)) {
            expires = jwtTokenConfig.getAccessExpires();
        } else if (TokenType.REFRESH.equals(type)) {
            expires = jwtTokenConfig.getRefreshExpires();
        }
        return expires;
    }
}
