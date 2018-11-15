package com.tybest.thirft.zookeeper.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * The method's name which is depend on linux command
 *
 * @author tb
 * @date 2018/11/14 15:06
 */
public final class PathUtils {

    public static final String SEPERATOR = "/";


    private static List<String> seperate(String path) {
        List<String> tokenize = new ArrayList<>();
        String[] toks = path.split(SEPERATOR);
        for(String tok: toks) {
            if(StringUtils.isNotEmpty(tok)){
                tokenize.add(tok);
            }
        }
        return tokenize;
    }

    private static String aggregate(List<String> toks) {
        StringBuilder sb = new StringBuilder();
        sb.append(SEPERATOR);
        for(int i=0,len = toks.size();i<len;i++) {
            sb.append(toks.get(i));
            if(i< (len-1)) {
                sb.append(SEPERATOR);
            }
        }
        return sb.toString();
    }

    /**
     *  正常化路径
     * @param path
     * @return
     */
    public static String normalize(String path) {
        return aggregate(seperate(path));
    }

    public static String parentPath(String path) {
        List<String> toks = seperate(path);
        int size = toks.size();
        if (size > 0) {
            toks.remove(size - 1);
        }
        return aggregate(toks);
    }

}
