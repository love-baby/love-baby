package com.love.baby.mis.controller;

import com.love.baby.common.bean.UploadFile;
import com.love.baby.common.common.UserSessionCommon;
import com.love.baby.common.exception.SystemException;
import com.love.baby.mis.config.SystemConfig;
import com.love.baby.mis.service.MusicService;
import com.love.baby.mis.service.UploadFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

/**
 * @author liangbc
 * @date 2018/6/14
 */
@RestController
@RequestMapping("/tool")
public class ToolCotroller {

    private static Logger logger = LoggerFactory.getLogger(ToolCotroller.class);

    @Resource
    private UploadFileService uploadFileService;

    @Resource
    private UserSessionCommon userSessionCommon;

    @Resource
    private MusicService musicService;

//    @PostMapping(value = "/analyze/music")
//    public void music(@RequestParam("file") MultipartFile file, @RequestHeader(value = "token") String token) throws InvalidDataException, IOException, UnsupportedTagException {
//        String userId = userSessionCommon.assertSessionAndGetUid(token);
//        UploadFile uploadFile = fileUpload(file, token);
//        Mp3File mp3file = new Mp3File(uploadFile.getPath());
//
//        Music music = Music.builder().
//                id(UUID.randomUUID().toString().replaceAll("-", "")).
//                createTime(new Date())
//                .path(uploadFile.getPath())
//                .build();
//        if (mp3file.hasId3v1Tag()) {
//            ID3v1 id3v1 = mp3file.getId3v1Tag();
//            music.setName(id3v1.getTitle());
//            music.setAlbumId(id3v1.getAlbum());
//            music.setAuthorId(id3v1.getArtist());
//        }
//        if (mp3file.hasId3v2Tag()) {
//            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
//            music.setName(id3v2Tag.getTitle());
//            music.setAlbumId(id3v2Tag.getAlbum());
//            music.setAuthorId(id3v2Tag.getArtist());
//            byte[] albumImageData = id3v2Tag.getAlbumImage();
//            if (albumImageData != null) {
//                String coverPath = new File(uploadFile.getPath()).getPath() + ".png";
//                RandomAccessFile randomAccessFile = new RandomAccessFile(coverPath, "rw");
//                randomAccessFile.write(albumImageData);
//                randomAccessFile.close();
//                music.setCoverPath(coverPath);
//            }
//        }
//        musicService.save(music);
//    }

    /**
     * 上传文件
     *
     * @param multipartfiles
     * @throws IOException
     */
    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
    public List<Map> fileUpload(@RequestParam(value = "files[]") MultipartFile[] multipartfiles) throws SystemException {
        List<Map> list = new ArrayList<>();
        for (MultipartFile file : multipartfiles) {
            logger.info(file.getOriginalFilename() + " fileUpload! ");
            Map fileMeta = new HashMap();
            fileMeta.put("fileName", file.getOriginalFilename());
            fileMeta.put("fileSize", file.getSize() / 1024 + " Kb");
            fileMeta.put("fileType", file.getContentType());
            fileMeta.put("id", UUID.randomUUID().toString().replaceAll("-", ""));
            try {
                //文件存放目录
                String path = SystemConfig.systemPath + File.separator + "upload" + File.separator + LocalDate.now();
                File filePath = new File(path);
                if (!filePath.exists() && !filePath.isDirectory()) {
                    if (!filePath.mkdirs()) {
                        logger.error("创建目标文件所在目录失败！");
                        throw new SystemException(500, "创建目标文件所在目录失败");
                    }
                }
                //文件类型
                String type = file.getContentType();
                //原始文件类型
                String originFileName = file.getOriginalFilename();
                //文件后缀
                String suffix = originFileName.substring(originFileName.lastIndexOf(".") + 1);
                //文件最后存放位置的全路径
                path = path + File.separator + System.currentTimeMillis() + "_" + DigestUtils.md5DigestAsHex(originFileName.getBytes()) + suffix;
                BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(path));
                buffStream.write(file.getBytes());
                buffStream.close();
                UploadFile uploadFile = UploadFile.builder()
                        .id(fileMeta.get("id").toString())
                        .createTime(new Date())
                        .name(originFileName)
                        .path(path)
                        .fileType(type)
                        .build();
                uploadFileService.save(uploadFile);
                fileMeta.put("path", path);
            } catch (IOException e) {
                logger.error("上传失败", e);
            }
            list.add(fileMeta);
        }
        return list;
    }
}
