package com.tybest.security.web.auth;

import com.tybest.security.model.TAccountPrincipal;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author tb
 * @date 2018/11/29 10:48
 */
@RequiredArgsConstructor
public class UrlAccessAuthentication implements Authentication {

    @NonNull
    @Getter
    private TAccountPrincipal accountPrincipal;

    @NonNull
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return accountPrincipal;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return accountPrincipal.getUsername();
    }
}
