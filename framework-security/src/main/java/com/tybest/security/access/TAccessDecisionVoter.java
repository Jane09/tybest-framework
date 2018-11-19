package com.tybest.security.access;

import lombok.Getter;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;

/**
 * @author tb
 * @date 2018/11/19 14:04
 */
public class TAccessDecisionVoter implements AccessDecisionVoter<FilterInvocation> {


    @Getter
    private boolean isSupervisorAlwaysGranted = true;

    public TAccessDecisionVoter supervisorGranted(boolean granted) {
        this.isSupervisorAlwaysGranted = granted;
        return this;
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }

    @Override
    public int vote(Authentication authentication, FilterInvocation filterInvocation, Collection<ConfigAttribute> collection) {
        return 0;
    }
}
