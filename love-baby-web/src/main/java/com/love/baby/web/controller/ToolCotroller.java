package com.love.baby.web.controller;

import com.love.baby.common.bean.Music;
import com.love.baby.common.bean.UploadFile;
import com.love.baby.common.common.UserSessionCommon;
import com.love.baby.web.exception.SystemException;
import com.love.baby.web.service.MusicService;
import com.love.baby.web.service.UploadFileService;
import com.mpatric.mp3agic.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import static com.love.baby.web.config.SystemConfig.SystemPath;

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

    @PostMapping(value = "/analyze/music")
    public void music(@RequestParam("file") MultipartFile file, @RequestHeader(value = "token") String token) throws InvalidDataException, IOException, UnsupportedTagException {
        String userId = userSessionCommon.assertSessionAndGetUid(token);
        UploadFile uploadFile = fileUpload(file, token);
        Mp3File mp3file = new Mp3File(uploadFile.getPath());

        Music music = Music.builder().
                id(UUID.randomUUID().toString().replaceAll("-", "")).
                createTime(new Date())
                .path(uploadFile.getPath())
                .build();
        if (mp3file.hasId3v1Tag()) {
            ID3v1 id3v1 = mp3file.getId3v1Tag();
            music.setName(id3v1.getTitle());
            music.setAlbumId(id3v1.getAlbum());
            music.setAuthorId(id3v1.getArtist());
        }
        if (mp3file.hasId3v2Tag()) {
            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
            music.setName(id3v2Tag.getTitle());
            music.setAlbumId(id3v2Tag.getAlbum());
            music.setAuthorId(id3v2Tag.getArtist());
            byte[] albumImageData = id3v2Tag.getAlbumImage();
            if (albumImageData != null) {
                String coverPath = new File(uploadFile.getPath()).getPath() + ".png";
                RandomAccessFile randomAccessFile = new RandomAccessFile(coverPath, "rw");
                randomAccessFile.write(albumImageData);
                randomAccessFile.close();
                music.setCoverPath(coverPath);
            }
        }
        musicService.save(music);
    }

    /**
     * 上传文件
     *
     * @param file
     * @param token
     * @throws IOException
     */
    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
    public UploadFile fileUpload(@RequestParam("file") MultipartFile file, @RequestHeader(value = "token") String token) throws SystemException, IOException {
        String userId = userSessionCommon.assertSessionAndGetUid(token);
        //文件名称
        String path = SystemPath + File.separator + "upload" + File.separator + LocalDate.now();
        File filePath = new File(path);
        String type = file.getContentType();
        String originFileName = file.getOriginalFilename();
        path = path + File.separator + System.currentTimeMillis() + originFileName;
        if (!filePath.exists() && !filePath.isDirectory()) {
            if (!filePath.mkdirs()) {
                logger.error("创建目标文件所在目录失败！");
                throw new SystemException(500, "创建目标文件所在目录失败");
            }
        }
        byte[] bytes = file.getBytes();
        BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(path));
        buffStream.write(bytes);
        buffStream.close();
        UploadFile uploadFile = UploadFile.builder()
                .id(UUID.randomUUID().toString().replaceAll("-", ""))
                .createTime(new Date())
                .name(originFileName)
                .path(path)
                .fileType(type)
                .build();
        uploadFileService.save(uploadFile);
        return uploadFile;
    }
}
