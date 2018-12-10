package com.tybest.tcc.bean;

import com.tybest.tcc.exception.InternalException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tb
 * @date 2018/12/10 17:25
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FactoryBuilder {

    private static List<BeanFactory> factories = new ArrayList<>();



    @AllArgsConstructor
    private static class SingletonFactory<T> {
        private volatile T instance;
        private String className;

        public SingletonFactory(Class<T> clazz) {
            this.className = clazz.getName();
        }

        @SuppressWarnings("unchecked")
        public T getInstance() {
            if(null == instance) {
                synchronized (SingletonFactory.class){
                    if(null == instance) {
                        ClassLoader loader = Thread.currentThread().getContextClassLoader();
                        Class<?> clazz = null;
                        try {
                            clazz = loader.loadClass(className);
                            instance = (T) clazz.newInstance();
                        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                            throw new InternalException("Failed to create instance of "+this.className,e);
                        }
                    }
                }
            }
            return instance;
        }

        @Override
        public int hashCode() {
            return this.className.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            SingletonFactory that = (SingletonFactory) obj;
            return className.equals(that.className);
        }
    }
}
