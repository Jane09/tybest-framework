package com.tybest.security.token;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author tb
 * @date 2018/11/19 13:42
 */
@Getter
@RequiredArgsConstructor
public class TokenData {

    private final String userId;
    private final String username;
    private final String clientId;

    @Setter
    private String clientType;
    @Setter
    private String clientVersion;
}
