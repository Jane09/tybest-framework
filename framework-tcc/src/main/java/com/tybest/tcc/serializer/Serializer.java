package com.tybest.tcc.serializer;

public interface Serializer<T> {

    /**
     * 序列化对象
     * @param data
     * @return
     */
    byte[] serialize(T data);

    /**
     * 反序列化
     * @param bytes
     * @return
     */
    T deserialize(byte[] bytes);


    /**
     * 对象拷贝
     * @param data
     * @return
     */
    T clone(T data);
}
