package com.love.baby.tool.async;

import com.alibaba.fastjson.JSON;
import com.love.baby.common.dto.QiNiuUploadDto;
import com.love.baby.common.stream.MusicConversionStream;
import com.love.baby.common.util.QiNiuUtil;
import com.love.baby.tool.config.SystemConfig;
import com.love.baby.tool.util.CmdUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author 23770
 */
@Service
@EnableBinding({MusicConversionStream.class})
public class AsyncTaskService {

    private static Logger logger = LoggerFactory.getLogger(AsyncTaskService.class);

    @Resource
    private MusicConversionStream musicConversionStream;

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
        //发送文件处理消息
        musicConversionStream.musicConversionOutput().send(MessageBuilder.withPayload(qiNiuUploadDto).build());
        logger.info("处理上传结果 qiNiuUploadDto = {}", JSON.toJSON(qiNiuUploadDto));
        if (f.delete()) {
            logger.info("转换后文件删除成功！ f = {}", f.getPath());
        }
    }

    /**
     * 转码完成之后上传七牛
     *
     * @param source
     */
    @Async
    public void executeMusicTask(String source) {
        if (StringUtils.isBlank(source)) {
            logger.error("资源文件不存在");
            return;
        }
        File file = new File(source);
        String outputPath = file.getParentFile().getPath() + File.separator + System.currentTimeMillis() + ".mp3";
        String cmd = "ffmpeg -i " + source + " -f mp3 -acodec libmp3lame -y " + outputPath;
        logger.info("转换开始 cmd = {}", cmd);
        String r = CmdUtil.execCmd(cmd, new File(SystemConfig.SystemTempPath + "/cmd"));
        logger.info("转换结束 r = {}", r);
        File f = new File(outputPath);
        if (f.exists()) {
            executeQiNiuUploadAsyncTask(outputPath);
        } else {
            logger.info("文件不存在，转换失败");
        }
        if (file.delete()) {
            logger.info("目标文件删除成功 f = {}", file.getPath());
        }
    }

}