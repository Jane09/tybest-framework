package com.tybest.security.web;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author tb
 * @date 2018/11/21 16:03
 */

public final class ClientDetails implements Serializable {

    private static final List<String> PROXY_HEADER = Arrays.asList("X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP");





}
