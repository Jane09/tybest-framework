package com.tybest.security.web.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author tb
 * @date 2018/11/29 11:02
 */
@Getter
@Setter
@RequiredArgsConstructor
public class UrlAuthority implements GrantedAuthority {

    private final String url;
    private String method;

    @Override
    public String getAuthority() {
        return url + ((method != null && method.trim().length() > 0) ? ":" + method : "");
    }
}
