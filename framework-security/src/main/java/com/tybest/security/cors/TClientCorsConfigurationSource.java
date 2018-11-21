package com.tybest.security.cors;

import com.tybest.security.config.SecurityConfig;
import io.jsonwebtoken.lang.Assert;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNullApi;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 配置cors
 * @author tb
 * @date 2018/11/19 13:57
 */
public class TClientCorsConfigurationSource implements CorsConfigurationSource {

    private static final List<String> DEFAULT_ALLOWED_METHODS;
    private static final List<String> DEFAULT_ALLOWED_HEADERS = Collections.unmodifiableList(Collections.singletonList(CorsConfiguration.ALL));
    private static final Long DEFAULT_MAX_AGE = 3600L;

    static {
        List<String> methods = new ArrayList<>();
        Arrays.asList(HttpMethod.GET, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PUT, HttpMethod.PATCH).forEach(method -> methods.add(method.name()));
        DEFAULT_ALLOWED_METHODS = Collections.unmodifiableList(methods);
    }

    private final SecurityConfig securityConfig;

    public TClientCorsConfigurationSource(SecurityConfig securityConfig) {
        Assert.notNull(securityConfig.getCors(),"The cors config can not be null!");
        Assert.notEmpty(securityConfig.getCors().getAllowedOrigins(),"Please config the corse allowedOrigins");
        this.securityConfig = securityConfig;
    }

    @Override
    @ParametersAreNonnullByDefault
    public CorsConfiguration getCorsConfiguration(HttpServletRequest httpServletRequest) {
        CorsConfiguration cc = new CorsConfiguration();
        cc.setAllowedHeaders(DEFAULT_ALLOWED_HEADERS);
        cc.setAllowedMethods(DEFAULT_ALLOWED_METHODS);
        cc.setAllowedOrigins(this.securityConfig.getCors().getAllowedOrigins());
        cc.setAllowCredentials(true);
        cc.setMaxAge(DEFAULT_MAX_AGE);
        return cc;
    }
}
