package com.tybest.seckill.repository;

import com.tybest.seckill.query.NativeQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author tb
 * @date 2018/12/4 9:56
 */
@Repository
@Slf4j
public class DynamicNativeQuery implements NativeQuery {

    @PersistenceContext
    private EntityManager em;

    private Query query(String sql, Object... params) {
        if(StringUtils.isBlank(sql)){
            throw new RuntimeException("sql is not empty.");
        }
        Query query = em.createNativeQuery(sql);
        if(null != params && params.length >0) {
            int i =1;
            for(Object param: params) {
                query.setParameter(i,param);
                i++;
            }
        }
        return query;
    }

    @Override
    public void save(Object entity) {
        em.persist(entity);
    }

    @Override
    public void update(Object entity) {
        em.merge(entity);
    }

    @Override
    public <T> void delete(Class<T> entityClass, Object entityid) {
        delete(entityClass,new Object[]{entityClass});
    }

    @Override
    public <T> void delete(Class<T> entityClass, Object[] entityids) {
        for (Object id : entityids) {
            em.remove(em.getReference(entityClass, id));
        }
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public <T> List<T> nativeQueryList(String sql, Object... params) {
        Query q = query(sql, params);
        q.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.TO_LIST);
        return q.getResultList();
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public <T> List<T> nativeQueryMap(String sql, Object... params) {
        Query q = query(sql, params);
        q.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return q.getResultList();
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public <T> List<T> nativeQueryModel(Class<T> targetClass, String sql, Object... params) {
        Query q = query(sql, params);
        q.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(targetClass));
        return q.getResultList();
    }

    @Override
    public Object nativeQueryObject(String sql, Object... params) {
        return query(sql,params).getSingleResult();
    }

    @Override
    public Object[] nativeQueryArray(String sql, Object... params) {
        return (Object[]) query(sql,params).getSingleResult();
    }

    @Override
    public int nativeExecuteUpdate(String sql, Object... params) {
        return query(sql,params).executeUpdate();
    }
}
