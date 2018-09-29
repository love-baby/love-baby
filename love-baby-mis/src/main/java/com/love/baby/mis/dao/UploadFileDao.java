package com.love.baby.mis.dao;

import com.love.baby.common.bean.UploadFile;
import com.love.baby.common.util.BaseDaoUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liangbc
 * @date 2018/7/18
 */
@Repository
public class UploadFileDao extends BaseDaoUtil<UploadFile> {

    /**
     * 根据MD5获取文件
     *
     * @param md5
     * @return
     */
    public UploadFile findByMd5(String md5) {
        String sql = "SELECT * from upload_file where md5 = ?";
        RowMapper<UploadFile> rowMapper = BeanPropertyRowMapper.newInstance(entityClass);
        List<UploadFile> list = jdbcTemplate.query(sql, rowMapper, md5);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
