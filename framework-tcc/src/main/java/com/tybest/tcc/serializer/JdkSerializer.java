package com.tybest.tcc.serializer;

import com.tybest.tcc.exception.InternalException;
import org.apache.commons.lang3.SerializationUtils;

import java.io.*;

/**
 * @author tb
 * @date 2018/12/11 10:30
 */
public class JdkSerializer<T extends Serializable> implements Serializer<T> {

    @Override
    public byte[] serialize(T data) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
             oos.writeObject(data);
             return baos.toByteArray();
        }catch (IOException e){
            throw new InternalException("serialize failed", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T deserialize(byte[] bytes) {
        if(null == bytes){
            throw new InternalException("The byte[] must not be null");
        }
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return (T)ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new InternalException("deserialize failed", e);
        }
    }

    @Override
    public T clone(T data) {
        return SerializationUtils.clone(data);
    }
}
