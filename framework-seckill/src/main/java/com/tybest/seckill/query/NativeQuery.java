package com.tybest.seckill.query;

import java.util.List;

public interface NativeQuery {

    /**
     * 保存
     * @param entity 实体
     */
    void save(Object entity);

    void update(Object entity);

    <T> void delete(Class<T> entityClass, Object entityid);

    <T> void delete(Class<T> entityClass, Object[] entityids);

    /**
     * query and return list data
     * @param sql   sql statement
     * @param params    parameters
     * @param <T>   result type
     * @return  return
     */
    <T> List<T> nativeQueryList(String sql, Object... params);

    <T> List<T> nativeQueryMap(String sql, Object... params);

    <T> List<T> nativeQueryModel(Class<T> targetClass, String sql, Object... params);

    Object nativeQueryObject(String sql, Object... params);

    Object[] nativeQueryArray(String sql, Object... params);

    int nativeExecuteUpdate(String sql, Object... params);
}
