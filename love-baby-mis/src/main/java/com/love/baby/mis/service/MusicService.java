package com.love.baby.mis.service;

import com.love.baby.common.bean.Music;
import com.love.baby.common.param.SearchParamsDto;
import com.love.baby.common.common.bean.PageUtil;
import com.love.baby.mis.dao.MusicDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangbc on 2018/7/4.
 */
@Service
public class MusicService implements BaseService<Music> {

    @Resource
    private MusicDao musicDao;

    @Override
    public void save(Music entity) {
        musicDao.save(entity);
    }

    @Override
    public void update(Music entity) {
        musicDao.update(entity);
    }

    @Override
    public void delete(Serializable id) {
        musicDao.delete(id);
    }

    @Override
    public void deleteAll() {
        musicDao.deleteAll();
    }

    @Override
    public Music findById(Serializable id) {
        return musicDao.findById(id);
    }

    @Override
    public List<Music> findAll() {
        return musicDao.findAll();
    }

    public PageUtil findAll(int cursor, int size, SearchParamsDto searchParamsDto) {
        String filterSql = null;
        List filterParams = new ArrayList();
        if (StringUtils.isNotBlank(searchParamsDto.getSearchText())) {
            filterSql = " and ( name = ? or author_id = ? )";
            filterParams.add(searchParamsDto.getSearchText());
            filterParams.add(searchParamsDto.getSearchText());
        }
        return musicDao.findAll(cursor, size, searchParamsDto, filterSql, filterParams);
    }
}
