package com.love.baby.tool.service;

import com.alibaba.fastjson.JSON;
import com.love.baby.common.exception.SystemException;
import com.love.baby.tool.async.AsyncTaskService;
import com.love.baby.tool.config.SystemConfig;
import com.love.baby.tool.util.CmdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liangbc
 * @date 2018/10/15
 */
@Service
public class ConverService {

    private static Logger logger = LoggerFactory.getLogger(ConverService.class);

    @Resource
    private AsyncTaskService asyncTaskService;

    /**
     * 缓存文件
     *
     * @param file
     * @return
     */
    public String cacheTemp(MultipartFile file) {
        Map fileMeta = new HashMap();
        fileMeta.put("fileName", file.getOriginalFilename());
        fileMeta.put("fileSize", file.getSize() / 1024 + " Kb");
        fileMeta.put("fileType", file.getContentType());
        logger.info("缓存文件 fileMeta = {}", JSON.toJSON(fileMeta));
        String path = SystemConfig.SystemTempPath + file.getOriginalFilename();
        try {
            BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(path));
            buffStream.write(file.getBytes());
            buffStream.close();
        } catch (IOException e) {
            logger.error("缓存文件失败", e);
            throw new SystemException(500, "缓存文件失败");
        }
        return path;
    }

    /**
     * 转换
     *
     * @param source
     * @return
     */
    public void conversion(String source) {
        File file = new File(source);
        String outputPath = file.getParentFile().getPath() + File.separator + System.currentTimeMillis() + ".mp3";
        String cmd = "ffmpeg -i " + source + " -f mp3 -acodec libmp3lame -y " + outputPath;
        logger.info("转换开始 cmd = {}", cmd);
        String r = CmdUtil.execCmd(cmd, new File(SystemConfig.SystemTempPath + "/cmd"));
        logger.info("转换结束 r = {}", r);
        File f = new File(outputPath);
        if (f.exists()) {
            asyncTaskService.executeQiNiuUploadAsyncTask(outputPath);
        } else {
            logger.info("文件不存在，转换失败");
        }
        if (file.delete()) {
            logger.info("目标文件删除成功 f = {}", file.getPath());
        }
    }

}
