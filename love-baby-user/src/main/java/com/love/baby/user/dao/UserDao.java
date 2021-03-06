package com.love.baby.user.dao;

import com.love.baby.common.bean.User;
import com.love.baby.common.util.BaseDaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liangbc
 * @date 2018/6/27
 */
@Repository
public class UserDao extends BaseDaoUtil<User> {


    private static Logger logger = LoggerFactory.getLogger(UserDao.class);

    /**
     * 根据用户名获取用户
     *
     * @param name
     * @return
     */
    public User findByName(String name) {
        String sql = "select * from user where name = ?";
        RowMapper<User> rowMapper = BeanPropertyRowMapper.newInstance(entityClass);
        List<User> list = jdbcTemplate.query(sql, rowMapper, name);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
