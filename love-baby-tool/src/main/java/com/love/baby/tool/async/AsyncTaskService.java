package com.love.baby.tool.async;

import com.alibaba.fastjson.JSON;
import com.love.baby.common.dto.QiNiuUploadDto;
import com.love.baby.common.util.QiNiuUtil;
import com.love.baby.tool.service.ConverService;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    private ConverService converService;

    /**
     * 上传七牛
     *
     * @param path
     */
    @Async
    public void executeQiNiuUploadAsyncTask(String path) {
        QiNiuUploadDto qiNiuUploadDto = QiNiuUploadDto.builder()
                .code(QiNiuUploadDto.Code.ERROR).message("上传失败").build();
        File f = new File(path);
        try {
            FileInputStream input = new FileInputStream(f);
            byte[] uploadBytes = new byte[input.available()];
            input.read(uploadBytes);
            String md5 = DigestUtils.md5Hex(uploadBytes);
            qiNiuUploadDto.setMd5(md5);
            String suffix = f.getPath().substring(f.getPath().lastIndexOf("."));
            String url = QiNiuUtil.fileUpload(uploadBytes, QiNiuUtil.Bucket.MUSIC, DigestUtils.md5Hex(uploadBytes) + suffix);
            qiNiuUploadDto.setQiNiuUrl(url);
            qiNiuUploadDto.setCode(QiNiuUploadDto.Code.OK);
            qiNiuUploadDto.setMessage("上传成功");
            logger.info("七牛上传成功");
        } catch (Exception e) {
            logger.error("七牛上传失败！", e);
            qiNiuUploadDto.setMessage(e.getMessage());
        }
        logger.info("处理上传结果 qiNiuUploadDto = {}", JSON.toJSON(qiNiuUploadDto));
        if (f.delete()) {
            logger.info("转换后文件删除成功！ f = {}", f.getPath());
        }
    }

    /**
     * 缓存本地
     *
     * @param file
     */
    @Async
    public void executeMusicTask(MultipartFile file) {
        String path = converService.cacheTemp(file);
        converService.conversion(path);
    }
}