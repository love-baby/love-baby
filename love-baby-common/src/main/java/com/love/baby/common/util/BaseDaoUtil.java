package com.love.baby.common.util;

import com.love.baby.common.exception.SystemException;
import com.love.baby.common.param.SearchParamsDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 23770
 */
public abstract class BaseDaoUtil<T> {

    private static final Logger logger = LoggerFactory.getLogger(BaseDaoUtil.class);
    /**
     * 设置一些操作的常量
     */
    private static final String SQL_INSERT = "insert";
    private static final String SQL_UPDATE = "update";
    private static final Pattern p = Pattern.compile("[A-Z]");

    @Resource
    public JdbcTemplate jdbcTemplate;

    public Class<T> entityClass;

    public BaseDaoUtil() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        entityClass = (Class<T>) type.getActualTypeArguments()[0];
        logger.info("Dao实现类是：" + entityClass.getName());
    }

    public void save(T entity) {
        String sql = this.makeSql(SQL_INSERT);
        Object[] args = this.setArgs(entity, SQL_INSERT);
        logger.info("save sql = {}", sql);
        jdbcTemplate.update(sql, args);
    }

    public void update(T entity) {
        String sql = this.makeSql(SQL_UPDATE);
        Object[] args = this.setArgs(entity, SQL_UPDATE);
        logger.info("update sql={}", sql);
        jdbcTemplate.update(sql, args);
    }

    public void delete(Serializable id) {
        String sql = " DELETE FROM " + camel4underline(entityClass.getSimpleName()) + " WHERE id=?";
        jdbcTemplate.update(sql, id);
    }

    public void deleteAll() {
        String sql = " TRUNCATE TABLE " + camel4underline(entityClass.getSimpleName());
        jdbcTemplate.execute(sql);
    }

    public T findById(Serializable id) {
        String sql = "SELECT * FROM " + camel4underline(entityClass.getSimpleName()) + " WHERE id=?";
        logger.info("findById sql={}", sql);
        RowMapper<T> rowMapper = BeanPropertyRowMapper.newInstance(entityClass);
        List<T> list = jdbcTemplate.query(sql, rowMapper, id);
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    public List<T> findAll() {
        String sql = "SELECT * FROM " + camel4underline(entityClass.getSimpleName());
        logger.info("findAll sql={}", sql);
        RowMapper<T> rowMapper = BeanPropertyRowMapper.newInstance(entityClass);
        return jdbcTemplate.query(sql, rowMapper);
    }

    /**
     * 获取所有
     *
     * @param cursor
     * @param size
     * @param searchParamsDto
     * @return
     */
    public PageUtil findAll(int cursor, int size, SearchParamsDto searchParamsDto, String filterSql) {
        String sql = "select * from " + camel4underline(entityClass.getSimpleName()) + " where 1 = 1 ";
        String sqlCount = "select count(1) from " + camel4underline(entityClass.getSimpleName()) + " where 1 = 1 ";
        List params = new ArrayList();
        List paramsCount = new ArrayList();
        if (StringUtils.isNotBlank(filterSql)) {
            sql += filterSql;
            sqlCount += filterSql;
        }
        if (StringUtils.isNotBlank(searchParamsDto.getDateMin())) {
            sql += " and create_time >= ?";
            sqlCount += " and create_time >= ?";
            params.add(searchParamsDto.getDateMin() + " 00:00:00");
            paramsCount.add(searchParamsDto.getDateMin() + " 00:00:00");
        }
        if (StringUtils.isNotBlank(searchParamsDto.getDateMax())) {
            sql += " and create_time <= ?";
            sqlCount += " and create_time <= ?";
            params.add(searchParamsDto.getDateMax() + " 23:59:59");
            paramsCount.add(searchParamsDto.getDateMax() + " 23:59:59");
        }
        if (StringUtils.isNotBlank(searchParamsDto.getSortField()) && StringUtils.isNotBlank(searchParamsDto.getSort())) {
            sql += " order by " + searchParamsDto.getSortField() + " " + searchParamsDto.getSort();
        }
        sql += " limit ?,?";
        params.add(cursor);
        params.add(size);

        RowMapper<T> rowMapper = BeanPropertyRowMapper.newInstance(entityClass);
        List<T> list = jdbcTemplate.query(sql, rowMapper, params.toArray());
        Integer count = jdbcTemplate.queryForObject(sqlCount, Integer.class, paramsCount.toArray());
        PageUtil pageUtil = PageUtil.builder()
                .data(list)
                .recordsFiltered(count)
                .recordsTotal(count).build();
        return pageUtil;
    }

    /**
     * 组装SQL
     *
     * @param sqlFlag
     * @return
     */
    private String makeSql(String sqlFlag) {
        StringBuffer sql = new StringBuffer();
        Field[] fields = entityClass.getDeclaredFields();
        if (sqlFlag.equals(SQL_INSERT)) {
            sql.append(" INSERT INTO " + camel4underline(entityClass.getSimpleName()));
            sql.append("(");
            for (int i = 0; fields != null && i < fields.length; i++) {
                fields[i].setAccessible(true);
                String column = fields[i].getName();
                sql.append(camel4underline(column)).append(",");
            }
            sql = sql.deleteCharAt(sql.length() - 1);
            sql.append(") VALUES (");
            for (int i = 0; fields != null && i < fields.length; i++) {
                sql.append("?,");
            }
            sql = sql.deleteCharAt(sql.length() - 1);
            sql.append(")");
        } else if (sqlFlag.equals(SQL_UPDATE)) {
            sql.append(" UPDATE " + camel4underline(entityClass.getSimpleName()) + " SET ");
            for (int i = 0; fields != null && i < fields.length; i++) {
                fields[i].setAccessible(true);
                String column = fields[i].getName();
                if (column.equals("id")) {
                    continue;
                }
                sql.append(camel4underline(column)).append("=").append("?,");
            }
            sql = sql.deleteCharAt(sql.length() - 1);
            sql.append(" WHERE id=?");
        }
        logger.info("SQL : " + sql);
        return sql.toString();

    }

    /**
     * 拼接参数
     *
     * @param entity
     * @param sqlFlag
     * @return
     * @throws IllegalAccessException
     */
    private Object[] setArgs(T entity, String sqlFlag) {
        Field[] fields = entityClass.getDeclaredFields();
        Object[] tempArr = new Object[fields.length];
        for (int i = 0; tempArr != null && i < tempArr.length; i++) {
            fields[i].setAccessible(true);
            try {
                tempArr[i] = fields[i].get(entity);
            } catch (IllegalAccessException e) {
                logger.error("参数拼装失败", e);
                throw new SystemException(500, "参数拼装失败！");
            }
        }
        if (sqlFlag.equals(SQL_UPDATE)) {
            Object[] args = new Object[fields.length];
            System.arraycopy(tempArr, 1, args, 0, tempArr.length - 1);
            args[args.length - 1] = tempArr[0];
            return args;
        }
        return tempArr;

    }


    /**
     * 对象驼峰写法转成下划线的
     *
     * @param param
     * @return
     */
    private static String camel4underline(String param) {
        if (StringUtils.isBlank(param)) {
            return "";
        }
        StringBuilder builder = new StringBuilder(param);
        Matcher mc = p.matcher(param);
        int i = 0;
        while (mc.find()) {
            builder.replace(mc.start() + i, mc.end() + i, "_" + mc.group().toLowerCase());
            i++;
        }
        if ('_' == builder.charAt(0)) {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }

}