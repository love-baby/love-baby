package com.love.baby.user.service;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author liangbc
 * @date 2018/6/28
 */
public interface BaseService<T> {

    void save(T entity);

    void update(T entity);

    void delete(Serializable id);

    void deleteAll();

    T findById(Serializable id);

    List<T> findAll();
}
