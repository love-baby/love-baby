package com.love.baby.user.service;

import com.love.baby.common.bean.User;
import com.love.baby.common.param.SearchParamsDto;
import com.love.baby.common.util.PageUtil;
import com.love.baby.user.dao.UserDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * Created by liangbc on 2018/6/28.
 */
@Service
public class UserService implements BaseService<User> {

    @Resource
    private UserDao userDao;

    @Override
    public void save(User entity) {
        userDao.save(entity);
    }

    @Override
    public void update(User entity) {
        userDao.update(entity);
    }

    @Override
    public void delete(Serializable id) {
        userDao.delete(id);
    }

    @Override
    public void deleteAll() {
        userDao.deleteAll();
    }

    @Override
    public User findById(Serializable id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    /**
     * 根据名称获取用户
     *
     * @param name
     * @return
     */
    public User findByName(String name) {
        return userDao.findByName(name);
    }

    /**
     * 根据名称获取用户s是否存在
     *
     * @param name
     * @return
     */
    public Boolean isExist(String name) {
        return userDao.findByName(name) == null ? false : true;
    }


    public PageUtil findAll(int cursor, int size, SearchParamsDto searchParamsDto) {
        String filterSql = null;
        if (StringUtils.isNotBlank(searchParamsDto.getSearchText())) {
            filterSql = " and name = '" + searchParamsDto.getSearchText() +"'";
        }
        return userDao.findAll(cursor, size, searchParamsDto, filterSql);
    }
}

