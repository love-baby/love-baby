package com.love.baby.tool.service;

import com.alibaba.fastjson.JSON;
import com.love.baby.common.exception.SystemException;
import com.love.baby.tool.config.SystemConfig;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
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
        //原始文件类型
        String originFileName = file.getOriginalFilename();
        String path = SystemConfig.SystemTempPath;
        try {
            path = path + DigestUtils.md5Hex(file.getBytes()) + originFileName.substring(originFileName.lastIndexOf("."));
            BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(path));
            buffStream.write(file.getBytes());
            buffStream.close();
        } catch (IOException e) {
            logger.error("缓存文件失败", e);
            throw new SystemException(500, "缓存文件失败");
        }
        return path;
    }

}
