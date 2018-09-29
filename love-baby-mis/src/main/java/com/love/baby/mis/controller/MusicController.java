package com.love.baby.mis.controller;

import com.alibaba.fastjson.JSON;
import com.love.baby.common.bean.Album;
import com.love.baby.common.bean.Author;
import com.love.baby.common.bean.Music;
import com.love.baby.common.common.UserSessionCommon;
import com.love.baby.common.exception.SystemException;
import com.love.baby.common.param.SearchParams;
import com.love.baby.common.param.SearchParamsDto;
import com.love.baby.common.util.PageUtil;
import com.love.baby.mis.service.MusicService;
import com.love.baby.mis.vo.MusicVo;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

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
     * 获取所有
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
        PageUtil pageUtil = musicService.findAll(Integer.parseInt(map.get("iDisplayStart")), Integer.parseInt(map.get("iDisplayLength")), searchParamsDto);
        List<MusicVo> list = new ArrayList<>();
        pageUtil.getData().forEach(music -> {
            //查询专辑
            Album album = new Album();
            //查询歌手信息
            Author author = new Author();
            list.add(new MusicVo(author, album, JSON.parseObject(JSON.toJSONString(music), Music.class)));
        });
        pageUtil.setData(list);
        return pageUtil;

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
     * 删除集合
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


    /**
     * 获取详情
     *
     * @param token
     * @return
     */
    @GetMapping("/{id}")
    public MusicVo music(@RequestHeader(value = "token") String token, @PathVariable String id) throws IOException, InvalidAudioFrameException, TagException, ReadOnlyFileException {
        logger.info("获取音乐信息 Id = {} ", id);
        String userId = userSessionCommon.assertSessionAndGetUid(token);
        Music music = musicService.findById(id);
        if (music == null) {
            return null;
        }
        //查询专辑
        Album album = new Album();
        //查询歌手信息
        Author author = new Author();
        MP3File mp3File = new MP3File(music.getPath());
        if (mp3File.hasID3v2Tag()) {
            AbstractID3v2Tag id3v2Tag = mp3File.getID3v2Tag();
            music.setName(id3v2Tag.getFirst(ID3v24Frames.FRAME_ID_TITLE));
            music.setAlbumId(id3v2Tag.getFirst(ID3v24Frames.FRAME_ID_ALBUM));
            music.setAuthorId(id3v2Tag.getFirst(ID3v24Frames.FRAME_ID_ARTIST));
        }
        musicService.update(music);
        return new MusicVo(author, album, JSON.parseObject(JSON.toJSONString(music), Music.class));
    }

    /**
     * 添加
     *
     * @param token
     * @param music
     */
    @PutMapping("")
    public void create(@RequestHeader(value = "token") String token, @RequestBody Music music) {
        logger.info("添加歌曲 music = {}", JSON.toJSON(music));
        String userId = userSessionCommon.assertSessionAndGetUid(token);
        music.setCreateTime(new Date());
        music.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        musicService.save(music);
    }

    /**
     * 修改
     *
     * @param token
     * @param music
     */
    @PutMapping("/update")
    public void update(@RequestHeader(value = "token") String token, @RequestBody Music music) {
        logger.info("添加歌曲 music = {}", JSON.toJSON(music));
        String userId = userSessionCommon.assertSessionAndGetUid(token);
        musicService.update(music);
    }
}
