package com.tybest.security.token;

public interface TokenProcessor {

    String generate(TokenData data, TokenType tokenType);

    TokenData retrieve(String token, TokenType tokenType);

    boolean needRefresh(String token);

}
