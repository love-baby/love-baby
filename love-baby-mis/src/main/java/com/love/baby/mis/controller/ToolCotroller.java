package com.love.baby.mis.controller;

import com.love.baby.common.bean.UploadFile;
import com.love.baby.common.common.UserSessionCommon;
import com.love.baby.common.exception.SystemException;
import com.love.baby.mis.config.SystemConfig;
import com.love.baby.mis.service.UploadFileService;
import org.apache.commons.codec.digest.DigestUtils;
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


    /**
     * 上传文件
     *
     * @param multipartfiles
     * @throws IOException
     */
    @PostMapping(value = "/fileUpload")
    public List<Map> fileUpload(@RequestParam(value = "files[]") MultipartFile[] multipartfiles) throws SystemException, IOException {
        List<Map> list = new ArrayList<>();
        for (MultipartFile file : multipartfiles) {
            logger.info(file.getOriginalFilename() + " fileUpload! ");
            Map fileMeta = new HashMap();
            fileMeta.put("fileName", file.getOriginalFilename());
            fileMeta.put("fileSize", file.getSize() / 1024 + " Kb");
            fileMeta.put("fileType", file.getContentType());
            fileMeta.put("id", UUID.randomUUID().toString().replaceAll("-", ""));

            String md5 = DigestUtils.md5Hex(file.getBytes());
            UploadFile uploadFile = uploadFileService.findByMd5(md5);
            if (uploadFile != null) {
                fileMeta.put("path", uploadFile.getPath());
                list.add(fileMeta);
                continue;
            }
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
                String suffix = originFileName.substring(originFileName.lastIndexOf("."));
                //文件最后存放位置的全路径
                path = path + File.separator + System.currentTimeMillis() + "_" + DigestUtils.md5Hex(originFileName.getBytes()) + suffix;
                BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(path));
                buffStream.write(file.getBytes());
                buffStream.close();
                uploadFile = UploadFile.builder()
                        .id(fileMeta.get("id").toString())
                        .createTime(new Date())
                        .name(originFileName)
                        .path(path)
                        .fileType(type)
                        .md5(md5)
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

    /**
     * 七牛鉴权接口
     * @param token
     * @param userip
     * @param authfrom
     */
    @GetMapping(value = "/cdnauth")
    public Integer cdnauth(@RequestParam(value = "token") String token, @RequestHeader(value = "userip") String userip, @RequestHeader(value = "authfrom") String authfrom) {
        logger.info("文件鉴权 token = {},userip = {},authfrom = {}", token, userip, authfrom);
        userSessionCommon.assertSessionAndGetUid(token);
        return 200;
    }
}
