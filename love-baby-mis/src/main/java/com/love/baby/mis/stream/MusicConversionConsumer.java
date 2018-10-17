package com.love.baby.mis.stream;

import com.alibaba.fastjson.JSON;
import com.love.baby.common.bean.UploadFile;
import com.love.baby.common.dto.QiNiuUploadDto;
import com.love.baby.common.stream.MusicConversionStream;
import com.love.baby.mis.service.UploadFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author liangbc
 * @date 2018/10/16
 */
@EnableBinding(MusicConversionStream.class)
@Service
public class MusicConversionConsumer {

    private static Logger logger = LoggerFactory.getLogger(MusicConversionConsumer.class);

    @Resource
    private UploadFileService uploadFileService;

    @StreamListener("music_conversion")
    public void inputQiNiuUploadOrder(QiNiuUploadDto qiNiuUploadDto) {
        logger.info("音乐文件处理结果 qiNiuUploadDto = {}", JSON.toJSON(qiNiuUploadDto));
        if (qiNiuUploadDto.getCode() == QiNiuUploadDto.Code.OK) {
            UploadFile uploadFile = uploadFileService.findByMd5(qiNiuUploadDto.getMd5());
            uploadFile.setQiNiuUrl(qiNiuUploadDto.getQiNiuUrl());
            uploadFileService.update(uploadFile);
        }
    }
}
