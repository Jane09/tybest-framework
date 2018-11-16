package com.tybest.security.config.metadata;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author tb
 * @date 2018/11/16 9:59
 */
@Getter
@Setter
@RequiredArgsConstructor
public class EncoderConfig {

    public static final String PWD_ENCODER_BCRYPT = "bcrypt";
    public static final String PWD_ENCODER_SCRYPT = "scrypt";
    public static final String PWD_ENCODER_PBKDF2 = "pbkdf2";
    public static final String PWD_ENCODER_MD5 = "md5";


    private String passwordEncoder;

    private String passwordSalt;
}
