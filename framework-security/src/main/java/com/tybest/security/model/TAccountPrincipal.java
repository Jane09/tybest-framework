package com.tybest.security.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

/**
 * @author tb
 * @date 2018/11/15 15:45
 */
@RequiredArgsConstructor
@Getter
public class TAccountPrincipal implements Principal {

    private final String id;
    private final String username;
    private final Boolean isSupervisor;

    @Override
    public String getName() {
        return this.username;
    }
}
