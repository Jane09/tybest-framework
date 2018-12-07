package com.tybest.tcc.utils;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * @author tb
 * @date 2018/12/7 15:28
 */
public final class IdUtils {

    public static void main(String[] args) {
        UUID uid = UUID.randomUUID();
        System.out.println(uid.toString());
        System.out.println(bytes2uuid(uuid2bytes(uid)).toString());
    }


    public static byte[] uuid2bytes(UUID uuid) {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        return buffer.array();
    }

    public static UUID bytes2uuid(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        long most = buffer.getLong();
        long least = buffer.getLong();
        return new UUID(most,least);
    }

    public static String getUUID(){
        return UUID.randomUUID().toString();
    }
}
