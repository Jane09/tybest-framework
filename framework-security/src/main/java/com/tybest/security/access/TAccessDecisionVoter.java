package com.tybest.security.access;

import com.tybest.security.web.auth.UrlAccessAuthentication;
import com.tybest.security.web.auth.UrlAuthority;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * 访问决策选举
 * @author tb
 * @date 2018/11/19 14:04
 */
@Slf4j
public class TAccessDecisionVoter implements AccessDecisionVoter<FilterInvocation> {

    /**
     * 认同
     */
    private static final int ACCESS_GRANTED = 1;
    /**
     * 反对
     */
    private static final int ACCESS_DENIED = -1;
    /**
     * 弃权
     */
    private static final int ACCESS_ABSTAIN = 0;

    @Getter
    private boolean isSupervisorAlwaysGranted = true;

    public TAccessDecisionVoter supervisorGranted(boolean granted) {
        this.isSupervisorAlwaysGranted = granted;
        return this;
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    @Override
    public int vote(Authentication authentication, FilterInvocation filterInvocation, Collection<ConfigAttribute> collection) {
        if(!(authentication instanceof UrlAccessAuthentication)) {
            return ACCESS_ABSTAIN;
        }
        UrlAccessAuthentication accessAuthentication = (UrlAccessAuthentication)authentication;
        if(isSupervisorAlwaysGranted && accessAuthentication.getAccountPrincipal().getIsSupervisor()) {
            return ACCESS_GRANTED;
        }
        HttpServletRequest request = filterInvocation.getHttpRequest();
        AntPathRequestMatcher urlMatcher;
        for (GrantedAuthority ga : accessAuthentication.getAuthorities()) {
            if (ga instanceof UrlAuthority) {
                UrlAuthority urlGA = (UrlAuthority) ga;
                urlMatcher = new AntPathRequestMatcher(urlGA.getUrl());
                if (urlMatcher.matches(request)) {
                    if (urlGA.getMethod() == null || urlGA.getMethod().trim().length() == 0 || urlGA.getMethod().equals(request.getMethod())) {
                        return ACCESS_GRANTED;
                    }
                }
            }
        }
        return ACCESS_DENIED;
    }
}
