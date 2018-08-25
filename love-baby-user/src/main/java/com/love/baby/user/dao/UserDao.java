package com.love.baby.user.dao;

import com.love.baby.common.bean.User;
import com.love.baby.common.util.PageUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

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
        List<User> list = jdbcTemplate.query(sql, rowMapper, name);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public PageUtil findAll(int cursor, int size) {
        String sql = "select * from user users LIMIT ?,?";
        RowMapper<User> rowMapper = BeanPropertyRowMapper.newInstance(entityClass);
        List<User> list = jdbcTemplate.query(sql, rowMapper, cursor, size);
        sql = "select count(1) from user";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        PageUtil pageUtil = PageUtil.builder()
                .data(list)
                .recordsFiltered(list.size())
                .recordsFiltered(count).build();
        return pageUtil;
    }
}
