package com.love.baby.mis.async;

import com.love.baby.common.bean.Music;
import com.love.baby.common.util.QiNiuUtil;
import com.love.baby.mis.service.MusicService;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author 23770
 */
@Service
public class AsyncTaskService {

    private static Logger logger = LoggerFactory.getLogger(AsyncTaskService.class);

    @Resource
    private MusicService musicService;

    @Async
    public void executeQiNiuUploadAsyncTask(Music music) {
        File f = new File(music.getPath());
        try {
            FileInputStream input = new FileInputStream(f);
            byte[] uploadBytes = new byte[input.available()];
            String suffix = f.getPath().substring(f.getPath().lastIndexOf("."));
            String url = QiNiuUtil.fileUpload(uploadBytes, QiNiuUtil.Bucket.MUSIC, DigestUtils.md5Hex(uploadBytes) + suffix);
            music.setQiNiuUrl(url);
            musicService.update(music);
            logger.info("七牛上传成功");
        } catch (Exception e) {
            logger.error("七牛上传失败！");
        }
    }
}