package com.tybest.security.cors;

import com.tybest.security.config.SecurityConfig;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;

/**
 * @author tb
 * @date 2018/11/19 13:57
 */
public class TClientCorsConfigurationSource implements CorsConfigurationSource {

    private final SecurityConfig securityConfig;

    public TClientCorsConfigurationSource(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
    }

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest httpServletRequest) {
        return null;
    }
}
