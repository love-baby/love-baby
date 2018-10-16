package com.love.baby.mis.stream;

import com.alibaba.fastjson.JSON;
import com.love.baby.common.dto.QiNiuUploadDto;
import com.love.baby.common.stream.QiNiuUploadStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

/**
 * @author liangbc
 * @date 2018/10/16
 */
@EnableBinding(QiNiuUploadDto.class)
public class QiNiuUploadServer {

    private static Logger logger = LoggerFactory.getLogger(QiNiuUploadServer.class);

    @StreamListener(QiNiuUploadStream.INPUT_QINIU_UPLOAD_ORDER)
    public void inputQiNiuUploadOrder(QiNiuUploadDto qiNiuUploadDto) {
        logger.info("音乐文件处理结果 qiNiuUploadDto = {}", JSON.toJSON(qiNiuUploadDto));
    }
}
