package com.love.baby.mis.controller;

import com.alibaba.fastjson.JSON;
import com.love.baby.common.common.UserSessionCommon;
import com.love.baby.common.exception.SystemException;
import com.love.baby.common.param.SearchParams;
import com.love.baby.common.param.SearchParamsDto;
import com.love.baby.common.util.PageUtil;
import com.love.baby.mis.service.MusicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author liangbc
 * @date 2018/9/13
 */
@RestController
@RequestMapping("/music")
public class MusicController {

    private static Logger logger = LoggerFactory.getLogger(MusicController.class);

    @Resource
    private UserSessionCommon userSessionCommon;

    @Resource
    private MusicService musicService;

    /**
     * 获取所有音乐
     *
     * @return
     */
    @PostMapping("/listAll")
    public PageUtil listAll(@RequestHeader(value = "token") String token, @RequestBody SearchParams[] searchParams) {
        String uId = userSessionCommon.assertSessionAndGetUid(token);
        logger.info("获取所有音乐 token = {},uId = {},searchParams = {}", token, uId, JSON.toJSONString(searchParams));
        Map<String, String> map = SearchParams.findAllparams(searchParams);
        logger.info("获取所有获取所有音乐搜索参数  Map = {}", JSON.toJSONString(map));
        SearchParamsDto searchParamsDto = SearchParamsDto.builder()
                .searchText(map.get("searchText"))
                .dateMax(map.get("dateMax"))
                .dateMin(map.get("dateMin"))
                .sortField(Integer.parseInt(map.get("iSortCol_0")) == 4 ? "create_time" : "create_time")
                .sort(map.get("sSortDir_0")).build();
        return musicService.findAll(Integer.parseInt(map.get("iDisplayStart")), Integer.parseInt(map.get("iDisplayLength")), searchParamsDto);
    }


    /**
     * 删除
     *
     * @param id
     * @param token
     */
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable String id, @RequestHeader(value = "token") String token) {
        logger.info("删除 id = {}", JSON.toJSONString(id));
        userSessionCommon.assertSessionAndGetUid(token);
        musicService.delete(id);
    }


    /**
     * 删除
     *
     * @param ids
     * @param token
     */
    @DeleteMapping(value = "/list")
    public void deleteAll(@RequestBody List<String> ids, @RequestHeader(value = "token") String token) {
        logger.info("删除 ids = {}", JSON.toJSONString(ids));
        userSessionCommon.assertSessionAndGetUid(token);
        if (ids == null) {
            throw new SystemException(500, "参数非法！");
        }
        for (String id : ids) {
            musicService.delete(id);
        }
    }
}
