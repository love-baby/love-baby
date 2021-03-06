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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
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
        Map<String, String> map = SearchParams.findAllParams(searchParams);
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
            Music musics = JSON.parseObject(JSON.toJSONString(music), Music.class);
            UploadFile uploadFile = uploadFileService.findById(musics.getFilePathId());
            list.add(new MusicVo(author, album, musics, uploadFile));
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
        UploadFile uploadFile = uploadFileService.findById(music.getFilePathId());
        File file = new File(uploadFile.getPath());
        try {
            AudioFile audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTag();
            music.setName(tag.getFirst(FieldKey.TITLE));
            music.setAlbumId(tag.getFirst(FieldKey.ALBUM));
            music.setAuthorId(tag.getFirst(FieldKey.ARTIST));
        } catch (Exception e) {
            logger.error("解析文件失败", e);
        }
        return new MusicVo(author, album, music, uploadFile);
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


        UploadFile uploadFile = uploadFileService.findById(musicOld.getFilePathId());
        File f = new File(uploadFile.getPath());
        AudioFile audioFile = AudioFileIO.read(f);
        Tag tag = audioFile.getTag();
        tag.setField(FieldKey.TITLE, musicOld.getName());
        tag.setField(FieldKey.ALBUM, musicOld.getAlbumId());
        tag.setField(FieldKey.ARTIST, musicOld.getAuthorId());
        audioFile.commit();

        //获取时长
        int trackLength = audioFile.getAudioHeader().getTrackLength();
        int min = trackLength / 60;
        int second = trackLength % 60;
        String length = min + ":" + second;
        musicOld.setTime(length);
        musicService.update(musicOld);

        FileInputStream input = new FileInputStream(audioFile.getFile());
        byte[] fileBytes = new byte[input.available()];
        input.read(fileBytes);
        String md5New = DigestUtils.md5Hex(fileBytes);

        //如果文件发送变化
        if (!StringUtils.equals(uploadFile.getMd5(), md5New)) {
            //原文件名
            String originFileName = f.getName();
            //后缀
            String suffix = originFileName.substring(originFileName.lastIndexOf("."));
            //新文件地址
            String newPath = f.getParentFile().getPath() + File.separator + md5New + suffix;
            BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(newPath));
            buffStream.write(fileBytes);
            buffStream.close();
            uploadFile.setMd5(md5New);
            uploadFile.setPath(newPath);
            uploadFileService.update(uploadFile);
            if (f.delete()) {
                logger.info("删除旧文件成功 path = {},新文件地址 newPath = {}", f.getPath(), newPath);
            }
        }

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
        UploadFile uploadFile = uploadFileService.findById(music.getFilePathId());
        String src = SystemConfig.web_host + uploadFile.getPath();
        //判断是否上传七牛
        if (StringUtils.isBlank(uploadFile.getQiNiuUrl())) {
            File file = new File(uploadFile.getPath());
            InputStream input = new FileInputStream(file);
            byte[] uploadBytes = new byte[input.available()];
            input.read(uploadBytes);
            MultipartFile multipartFile = new MultipartFileUtil(file.getName(), uploadBytes, uploadFile.getFileType());
            //处理转码和上传到七牛上去
            conversionRpcService.musicConversionMp3(multipartFile);
        } else {
            //回源鉴权防盗链
            //src = music.getQiNiuUrl() + "?token=" + token;
            //生成时间戳防盗链
            src = QiNiuUtil.getAntiLeechAccessUrlBasedOnTimestamp(uploadFile.getQiNiuUrl(), 360);
        }
        Map m = new HashMap();
        m.put("name", music.getName());
        m.put("singer", music.getAuthorId());
        m.put("img", "https://images.love-baby.vip/20181010180902.png");
        m.put("src", src);
        m.put("lrc", "");
        m.put("time", music.getTime());
        return m;
    }


}
