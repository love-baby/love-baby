package com.love.baby.tool.service;

import com.love.baby.common.exception.SystemException;
import com.love.baby.tool.config.SystemConfig;
import com.love.baby.tool.util.CmdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;

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
     * @param bytes
     * @param suffix
     * @return
     */
    public String cacheTemp(byte[] bytes, String suffix) {
        logger.info("缓存文件");
        String path = SystemConfig.SystemTempPath + System.currentTimeMillis() + suffix;
        try {
            BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(path));
            buffStream.write(bytes);
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
    public byte[] conversion(String source) {
        File file = new File(source);
        String outputPath = file.getParentFile().getPath() + File.separator + System.currentTimeMillis() + ".mp3";
        logger.info("path = {}", outputPath);
        String cmd = "ffmpeg -i " + source + " -f mp3 -acodec libmp3lame -y " + outputPath;
        CmdUtil.execCmd(cmd, new File(SystemConfig.SystemTempPath + "/cmd"));
        byte[] bytes = null;
        try {
            FileInputStream input = new FileInputStream(new File(outputPath));
            bytes = new byte[input.available()];
        } catch (FileNotFoundException e) {
            logger.error("转换失败", e);
        } catch (IOException e) {
            logger.error("转换失败", e);
        }
        return bytes;
    }
//    public static void main(String arr[]) {
//        conversion("D:\\data\\resource\\love-baby\\upload\\2018-07-181531903179551花僮-普通Disco.mp3");
//    }

}
