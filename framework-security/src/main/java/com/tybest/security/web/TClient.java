package com.tybest.security.web;

import lombok.Getter;
import lombok.Setter;

/**
 * @author tb
 * @date 2018/11/21 16:05
 */
@Getter
@Setter
public class TClient {
    private String clientId;
    private String clientAddress;
    private String clientType;
    private String clientVersion;
}
