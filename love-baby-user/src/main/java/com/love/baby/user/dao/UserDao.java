package com.love.baby.user.dao;

import com.alibaba.fastjson.JSON;
import com.love.baby.common.bean.User;
import com.love.baby.common.param.SearchUserParams;
import com.love.baby.common.util.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liangbc
 * @date 2018/6/27
 */
@Repository
public class UserDao extends BaseDao<User> {


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

    public PageUtil findAll(int cursor, int size, SearchUserParams searchUserParams) {
        String sql = "select * from user where 1 = 1";
        String sqlCount = "select count(1) from user where 1 = 1";
        List params = new ArrayList();
        List paramsCount = new ArrayList();
        if (StringUtils.isNotBlank(searchUserParams.getSearchText())) {
            logger.info("searchUserParams.getSearchText() = {}", searchUserParams.getSearchText());
            sql += " and name = ?";
            sqlCount += " and name = ?";
            params.add(searchUserParams.getSearchText());
            paramsCount.add(searchUserParams.getSearchText());
        }
        if (StringUtils.isNotBlank(searchUserParams.getDateMin())) {
            logger.info("searchUserParams.getDateMin() = {}", searchUserParams.getDateMin());
            sql += " and create_time >= ?";
            sqlCount += " and create_time >= ?";
            params.add(searchUserParams.getDateMin() + " 00:00:00");
            paramsCount.add(searchUserParams.getDateMin() + " 00:00:00");
        }
        if (StringUtils.isNotBlank(searchUserParams.getDateMax())) {
            logger.info("searchUserParams.getDateMax() = {}", searchUserParams.getDateMax());
            sql += " and create_time <= ?";
            sqlCount += " and create_time <= ?";
            params.add(searchUserParams.getDateMax() + " 23:59:59");
            paramsCount.add(searchUserParams.getDateMax() + " 23:59:59");
        }
        if (StringUtils.isNotBlank(searchUserParams.getSortField()) && StringUtils.isNotBlank(searchUserParams.getSort())) {
            logger.info("searchUserParams.getSortField() = {},searchUserParams.getSort() = {}", searchUserParams.getSortField(), searchUserParams.getSort());
            sql += " order by searchUserParams.getSortField() searchUserParams.getSort()";
        }
        sql += " limit ?,?";
        params.add(cursor);
        params.add(size);
        Object[] paramsArr = new Object[params.size()];
        for (int i = 0; i < params.size(); i++) {
            paramsArr[i]=params.get(i);
        }
        Object[] paramsCountArr = new Object[paramsCount.size()];
        for (int i = 0; i < paramsCount.size(); i++) {
            paramsCountArr[i]=paramsCount.get(i);
        }

        logger.info("sql = {},paramsArr = {}", sql, JSON.toJSON(paramsArr));
        logger.info("sqlCount = {},paramsCountArr = {}", sqlCount, JSON.toJSON(paramsCount.toArray()));

        RowMapper<User> rowMapper = BeanPropertyRowMapper.newInstance(entityClass);
        List<User> list = jdbcTemplate.query(sql, rowMapper, paramsArr);
        Integer count = jdbcTemplate.queryForObject(sqlCount, Integer.class,paramsCountArr);
        PageUtil pageUtil = PageUtil.builder()
                .data(list)
                .recordsFiltered(count)
                .recordsTotal(count).build();
        return pageUtil;
    }
}
