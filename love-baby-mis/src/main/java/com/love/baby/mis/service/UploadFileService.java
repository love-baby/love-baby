package com.love.baby.mis.service;

import com.alibaba.fastjson.JSON;
import com.love.baby.common.bean.UploadFile;
import com.love.baby.common.param.SearchParamsDto;
import com.love.baby.common.util.PageUtil;
import com.love.baby.mis.dao.UploadFileDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * @author liangbc
 * @date 2018/7/18
 */
@Service
public class UploadFileService implements BaseService<UploadFile> {

    private static Logger logger = LoggerFactory.getLogger(UploadFileService.class);

    @Resource
    private UploadFileDao uploadFileDao;

    @Override
    public void save(UploadFile entity) {
        uploadFileDao.save(entity);
    }

    @Override
    public void update(UploadFile entity) {
        uploadFileDao.update(entity);
    }

    @Override
    public void delete(Serializable id) {
        UploadFile uploadFile = uploadFileDao.findById(id);
        uploadFileDao.delete(id);
        if (uploadFile != null) {
            File file = new File(uploadFile.getPath());
            if (file.exists() && file.isFile()) {
                if (file.delete()) {
                    logger.info("删除单个文件" + JSON.toJSON(uploadFile) + "成功！");

                } else {
                    logger.info("删除单个文件" + JSON.toJSON(uploadFile) + "失败！");
                }
            } else {
                logger.info("删除单个文件失败 文件不存在" + JSON.toJSON(uploadFile));
            }
        }
    }

    @Override
    public void deleteAll() {
        uploadFileDao.deleteAll();
    }

    @Override
    public UploadFile findById(Serializable id) {
        return uploadFileDao.findById(id);
    }

    @Override
    public List<UploadFile> findAll() {
        return null;
    }


    public PageUtil findAll(int cursor, int size, SearchParamsDto searchUserParams) {
        return uploadFileDao.findAll(cursor, size, searchUserParams);
    }
}
