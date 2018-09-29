package com.love.baby.web.controller;

import com.love.baby.common.bean.UploadFile;
import com.love.baby.common.common.UserSessionCommon;
import com.love.baby.common.exception.SystemException;
import com.love.baby.web.service.MusicService;
import com.love.baby.web.service.UploadFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
