package com.tybest.security.token;

public interface TokenProcessor {

    /**
     * 生成token
     * @param data  token元数据
     * @param tokenType token类型
     * @return  返回token
     */
    String generate(TokenData data, TokenType tokenType);

    /**
     * token反解析
     * @param token token字符串
     * @param tokenType 类型
     * @return  返回token元数据
     */
    TokenData retrieve(String token, TokenType tokenType);

    /**
     * 判断是否需要刷新
     * @param token 字符串
     * @return  boolean
     */
    boolean needRefresh(String token);

}
