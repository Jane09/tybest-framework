package com.tybest.security.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author tb
 * @date 2018/11/15 15:42
 */
@Getter
@RequiredArgsConstructor
public class TAccount implements UserDetails {

    private final String id;
    private final String username;
    private final String encryptedPassword;
    @Setter
    private Collection<? extends GrantedAuthority> authorities = new HashSet<>();
    @Setter
    private boolean isSupervisor = false;
    @Setter
    private boolean enabled = true;
    @Setter
    private boolean accountExpired = false;
    @Setter
    private boolean accountLocked = false;
    @Setter
    private boolean passwordExpired = false;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return encryptedPassword;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !passwordExpired;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public TAccountPrincipal toPrincipal() {
        return new TAccountPrincipal(id, username, isSupervisor);
    }
}
