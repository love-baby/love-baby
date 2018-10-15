package com.love.baby.mis.controller;

import com.alibaba.fastjson.JSON;
import com.love.baby.common.api.ConversionRpcService;
import com.love.baby.common.bean.Album;
import com.love.baby.common.bean.Author;
import com.love.baby.common.bean.Music;
import com.love.baby.common.bean.UploadFile;
import com.love.baby.common.common.UserSessionCommon;
import com.love.baby.common.common.bean.PageUtil;
import com.love.baby.common.exception.SystemException;
import com.love.baby.common.param.SearchParams;
import com.love.baby.common.param.SearchParamsDto;
import com.love.baby.common.util.MultipartFileUtil;
import com.love.baby.common.util.QiNiuUtil;
import com.love.baby.mis.async.AsyncTaskService;
import com.love.baby.mis.config.SystemConfig;
import com.love.baby.mis.service.MusicService;
import com.love.baby.mis.service.UploadFileService;
import com.love.baby.mis.vo.MusicVo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    @Resource
    private AsyncTaskService asyncTaskService;

    @Resource
    private ConversionRpcService conversionRpcService;

    @Resource
    private UploadFileService uploadFileService;

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
    public MusicVo music(@RequestHeader(value = "token") String token, @PathVariable String id) throws IOException, InvalidAudioFrameException, TagException, ReadOnlyFileException, CannotReadException {
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
        File file = new File(music.getPath());
        try {
            AudioFile audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTag();
            music.setName(tag.getFirst(FieldKey.TITLE));
            music.setAlbumId(tag.getFirst(FieldKey.ALBUM));
            music.setAuthorId(tag.getFirst(FieldKey.ARTIST));
        } catch (Exception e) {
            logger.error("解析文件失败", e);
        }
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
    public void update(@RequestHeader(value = "token") String token, @RequestBody Music music) throws TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException, IOException, CannotWriteException {
        logger.info("修改歌曲 music = {}", JSON.toJSON(music));
        String userId = userSessionCommon.assertSessionAndGetUid(token);
        Music musicOld = musicService.findById(music.getId());
        musicOld.setName(music.getName() == null ? "" : music.getName());
        musicOld.setAlbumId(music.getAlbumId() == null ? "" : music.getAlbumId());
        musicOld.setAuthorId(music.getAuthorId() == null ? "" : music.getAuthorId());
        musicService.update(musicOld);

        AudioFile audioFile = AudioFileIO.read(new File(musicOld.getPath()));
        Tag tag = audioFile.getTag();
        tag.setField(FieldKey.TITLE, musicOld.getName());
        tag.setField(FieldKey.ALBUM, musicOld.getAlbumId());
        tag.setField(FieldKey.ARTIST, musicOld.getAuthorId());
        audioFile.commit();
    }


    /**
     * 试听
     *
     * @param token
     * @return
     */
    @GetMapping("/audition/{id}")
    public Map audition(@RequestHeader(value = "token") String token, @PathVariable String id) throws IOException {
        logger.info("获取音乐信息 Id = {} ", id);
        userSessionCommon.assertSessionAndGetUid(token);
        Music music = musicService.findById(id);
        if (music == null) {
            return null;
        }
        String src = SystemConfig.web_host + music.getPath();
        //判断是否上传七牛
        if (StringUtils.isBlank(music.getQiNiuUrl())) {
            //上传到七牛
            //asyncTaskService.executeQiNiuUploadAsyncTask(music);
            logger.info("执行转换任务");
            File file = new File(music.getPath());
            InputStream input = new FileInputStream(file);
            byte[] uploadBytes = new byte[input.available()];
            input.read(uploadBytes);
            String md5 = DigestUtils.md5Hex(uploadBytes);
            UploadFile uploadFile = uploadFileService.findByMd5(md5);
            logger.info("uploadFile = {}", JSON.toJSON(uploadFile));
            MultipartFile multipartFile = new MultipartFileUtil(file.getName(), uploadBytes, uploadFile.getFileType());
            ResponseEntity<byte[]> a = conversionRpcService.wavConversionMp3(multipartFile);
            if (a.getStatusCode() == HttpStatus.OK) {
                logger.info("成功");
            } else {
                logger.info("a = {}", JSON.toJSON(a));
            }
            logger.info("执行转换任务结束");
        } else {
            //回源鉴权防盗链
            //src = music.getQiNiuUrl() + "?token=" + token;
            //生成时间戳防盗链
            src = QiNiuUtil.getAntiLeechAccessUrlBasedOnTimestamp(music.getQiNiuUrl(), 360);
        }
        Map m = new HashMap();
        m.put("name", music.getName());
        m.put("singer", music.getAuthorId());
        m.put("img", "https://images.love-baby.vip/20181010180902.png");
        m.put("src", src);
        m.put("lrc", "");
        return m;
    }


}
