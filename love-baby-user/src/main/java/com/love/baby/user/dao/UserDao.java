package com.love.baby.user.dao;

import com.love.baby.common.bean.User;
import com.love.baby.common.param.SearchUserParams;
import com.love.baby.common.util.PageUtil;
import org.apache.commons.lang3.StringUtils;
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

    public PageUtil findAll(int cursor, int size, SearchUserParams searchUserParams) {
        String sql = "select * from user where 1 = 1";
        String sqlCount = "select count(1) from user where 1 = 1";
        Object[] params = null;
        Object[] paramsCount = null;
        if (StringUtils.isNotBlank(searchUserParams.getSearchText())) {
            sql += " and name = ?";
            sqlCount += " and name = ?";
            params = new Object[]{searchUserParams.getSearchText()};
            paramsCount = new Object[]{searchUserParams.getSearchText()};
        }
        if (StringUtils.isNotBlank(searchUserParams.getDateMin())) {
            sql += " and create_time >= ?";
            sqlCount += " and create_time >= ?";
            params = new Object[]{params, searchUserParams.getDateMin() + " 00:00:00"};
            paramsCount = new Object[]{paramsCount, searchUserParams.getDateMin() + " 00:00:00"};
        }
        if (StringUtils.isNotBlank(searchUserParams.getDateMax())) {
            sql += " and create_time <= ?";
            sqlCount += " and create_time <= ?";
            params = new Object[]{params, searchUserParams.getDateMax() + " 23:59:59"};
            paramsCount = new Object[]{paramsCount, searchUserParams.getDateMax() + " 23:59:59"};
        }
        if (StringUtils.isNotBlank(searchUserParams.getSortField()) && StringUtils.isNotBlank(searchUserParams.getSortField())) {
            sql += " order by ? ?";
            params = new Object[]{params, searchUserParams.getSortField(), searchUserParams.getSortField()};
        }
        sql += " limit ?,?";
        params = new Object[]{params, cursor, size};
        RowMapper<User> rowMapper = BeanPropertyRowMapper.newInstance(entityClass);
        List<User> list = jdbcTemplate.query(sql, rowMapper, params);
        Integer count = jdbcTemplate.queryForObject(sqlCount, Integer.class, paramsCount);
        PageUtil pageUtil = PageUtil.builder()
                .data(list)
                .recordsFiltered(count)
                .recordsTotal(count).build();
        return pageUtil;
    }
}
