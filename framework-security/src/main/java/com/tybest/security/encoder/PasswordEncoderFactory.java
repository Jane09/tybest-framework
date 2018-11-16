package com.tybest.security.encoder;

import com.tybest.security.config.metadata.EncoderConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.util.HashMap;
/**
 * @author tb
 * @date 2018/11/16 10:26
 */
@SuppressWarnings({"unchecked"})
public final class PasswordEncoderFactory {

    private static final HashMap PASSWORD_ENCODERS = new HashMap() {{
        PASSWORD_ENCODERS.put(EncoderConfig.PWD_ENCODER_BCRYPT, new BCryptPasswordEncoder());
        PASSWORD_ENCODERS.put(EncoderConfig.PWD_ENCODER_SCRYPT, new SCryptPasswordEncoder());
        PASSWORD_ENCODERS.put(EncoderConfig.PWD_ENCODER_PBKDF2, new Pbkdf2PasswordEncoder());
        PASSWORD_ENCODERS.put(EncoderConfig.PWD_ENCODER_MD5, new Md5PasswordEncoder());
    }};


    public static PasswordEncoder getEncoder(String encoderId) {
        return new DelegatingPasswordEncoder(encoderId, PASSWORD_ENCODERS);
    }
}
