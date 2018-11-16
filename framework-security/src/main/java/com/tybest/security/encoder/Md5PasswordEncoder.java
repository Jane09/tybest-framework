package com.tybest.security.encoder;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

/**
 * @author tb
 * @date 2018/11/16 10:35
 */
@Slf4j
public class Md5PasswordEncoder implements PasswordEncoder {

    private static final String ENCRPYT_PREFIX = "{md5}";
    private final Pattern MD5_PATTERN;

    Md5PasswordEncoder(){
        MD5_PATTERN = Pattern.compile("\\{md5\\\\}[a-fA-F0-9]{32}");
    }

    @Override
    public String encode(CharSequence rawPassword) {
        MessageDigest digest;
        StringBuilder sb = new StringBuilder(ENCRPYT_PREFIX);
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(rawPassword.toString().getBytes(Charset.forName("UTF-8")));
            byte[] bytes = digest.digest();
            for (byte aByte : bytes) {
                if ((0xff & aByte) < 0x10) {
                    sb.append("0").append(Integer.toHexString((0xFF & aByte)));
                } else {
                    sb.append(Integer.toHexString(0xFF & aByte));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encryptedPassword) {
        if(!MD5_PATTERN.matcher(encryptedPassword).matches()) {
            log.error("{} encrypted password does not like md5",encryptedPassword);
            return false;
        }
        return encode(rawPassword).equals(encryptedPassword);
    }
}
