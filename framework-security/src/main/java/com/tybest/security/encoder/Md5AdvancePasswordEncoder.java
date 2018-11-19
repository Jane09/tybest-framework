package com.tybest.security.encoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

/**
 * MD5 advance
 * @author tb
 */
public class Md5AdvancePasswordEncoder implements PasswordEncoder {

    private static final String ENCRPYT_PREFIX = "{md52}";
    public static void main(String[] args) {
        Md5AdvancePasswordEncoder encoder = new Md5AdvancePasswordEncoder("newtank");
        System.out.println(encoder.encode("111111"));
    }

    /**
     * ("淼龘F^")
     */
    private final String advance;

    public Md5AdvancePasswordEncoder(String advance) {
        this.advance = advance;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        if ((rawPassword == null) || ("".contentEquals(rawPassword))) {
            return null;
        }
        byte[] secretArr;
        byte[] byteArr = Base64.getEncoder().encode(rawPassword.toString().getBytes(StandardCharsets.UTF_8));
        byte[] key = advance.getBytes(StandardCharsets.UTF_8);
        final int len = key.length;
        final int blen = byteArr.length;
        secretArr = Arrays.copyOf(byteArr, blen + len);
        System.arraycopy(key, 0, secretArr, blen, len);
        return ENCRPYT_PREFIX+DigestUtils.md5DigestAsHex(secretArr);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encryptedPassword) {
        return encode(rawPassword).equals(encryptedPassword);
    }
}
