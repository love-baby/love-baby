package com.love.baby.web.service;

import com.love.baby.web.bean.UploadFile;
import com.love.baby.web.dao.UploadFileDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * @author liangbc
 * @date 2018/7/18
 */
@Service
public class UploadFileService implements BaseService<UploadFile> {

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
        uploadFileDao.delete(id);
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
}
