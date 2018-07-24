package com.love.baby.web.dao;

import com.love.baby.web.bean.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author liangbc
 * @date 2018/6/27
 */
@Repository
public class UserDao extends BaseDao<User> {
    /**
     * 根据用户名获取用户
     *
     * @param name
     * @return
     */
    public User findByName(String name) {
        String sql = "select * from user where name = ?";
        RowMapper<User> rowMapper = BeanPropertyRowMapper.newInstance(entityClass);
        return jdbcTemplate.queryForObject(sql, rowMapper, name);
    }
}
