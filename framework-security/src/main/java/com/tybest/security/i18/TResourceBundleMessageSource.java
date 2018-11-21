package com.tybest.security.i18;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * 消息
 * @author tb
 * @date 2018/11/21 14:34
 */
public final class TResourceBundleMessageSource extends ResourceBundleMessageSource {

    private static MessageSourceAccessor accessor = new MessageSourceAccessor(new TResourceBundleMessageSource());

    public TResourceBundleMessageSource() {
        this.setBasenames("com.tybest.i18.messages");
    }

    public static MessageSourceAccessor getAccessor(){
        return accessor;
    }
}
