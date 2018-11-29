package com.tybest.security.config;

import com.tybest.security.access.TAccessDecisionVoter;
import com.tybest.security.cors.TClientCorsConfigurationSource;
import com.tybest.security.service.TClientRememberMeService;
import com.tybest.security.token.JwtTokenProcessor;
import com.tybest.security.token.TokenProcessor;
import com.tybest.security.web.TAccessDeniedHandler;
import com.tybest.security.web.auth.TAuthenticationEntrypoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author tb
 * @date 2018/11/15 16:01
 */
public class TAuthenticateConfigurer {

    private final SecurityConfig securityConfig;
    private TokenProcessor tokenProcessor;

    public TAuthenticateConfigurer(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
        this.tokenProcessor = new JwtTokenProcessor(this.securityConfig.getJwtToken());
    }

    /**
     * 配置url过滤
     * @param http
     * @throws Exception
     */
    public void configureHttpSecurity(HttpSecurity http) throws Exception {
        //跨域、csrf攻击、记住我
        http.cors().configurationSource(new TClientCorsConfigurationSource(securityConfig))
            .and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().rememberMe().rememberMeServices(new TClientRememberMeService(this.tokenProcessor))
        ;
        //anon配置
        if (securityConfig.getIgnoreUrls() != null) {
            http.authorizeRequests().antMatchers(securityConfig.getIgnoreUrls().toArray(new String[0])).permitAll();
        }
        //拒绝访问设置
        http.authorizeRequests()
                .anyRequest()
                .authenticated()
                .accessDecisionManager(new AffirmativeBased(Arrays.asList(new WebExpressionVoter(),
                        new TAccessDecisionVoter().supervisorGranted(securityConfig.isSupervisorGranted()))));
        //异常拦截
        http.exceptionHandling()
                .accessDeniedHandler(new TAccessDeniedHandler())
                .authenticationEntryPoint(new TAuthenticationEntrypoint());
        //添加Filter过滤
        List<TFilter> filters = buildFilters();
        if (filters != null) {
            filters.forEach(tfilter -> {
                switch (tfilter.getPosition()) {
                    case AT:
                        http.addFilterAt(tfilter.getFilter(), tfilter.getReferencePositionClass());
                        break;
                    case BEFORE:
                        http.addFilterBefore(tfilter.getFilter(), tfilter.getReferencePositionClass());
                        break;
                    case AFTER:
                        http.addFilterAfter(tfilter.getFilter(), tfilter.getReferencePositionClass());
                        break;
                    default:
                }
            });
        }
    }

    /**
     * 配置AuthenticationProvider
     * @param auth
     */
    public void configureAuthenticationManagerBuilder(AuthenticationManagerBuilder auth) {
        List<AuthenticationProvider> providers = buildProviders();
        providers.forEach(auth::authenticationProvider);
    }


    private List<AuthenticationProvider> buildProviders() {
        List<AuthenticationProvider> providers = new ArrayList<>();

        return providers;
    }

    private List<TFilter> buildFilters() {
        List<TFilter> filters = new ArrayList<>();
//        filters.add(new TFilter());
        return filters;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    class TFilter {
        private Position position;
        private Filter filter;
        private Class<? extends Filter> referencePositionClass;
    }

    enum Position {
        BEFORE,AFTER,AT
    }
}
