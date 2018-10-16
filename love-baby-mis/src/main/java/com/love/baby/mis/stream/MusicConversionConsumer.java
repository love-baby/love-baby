package com.love.baby.mis.stream;

import com.alibaba.fastjson.JSON;
import com.love.baby.common.dto.QiNiuUploadDto;
import com.love.baby.common.stream.MusicConversionStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

/**
 * @author liangbc
 * @date 2018/10/16
 */
@EnableBinding(MusicConversionStream.class)
public class MusicConversionConsumer {

    private static Logger logger = LoggerFactory.getLogger(MusicConversionConsumer.class);

    @StreamListener(MusicConversionStream.MUSIC_CONVERSION_INPUT)
    public void inputQiNiuUploadOrder(QiNiuUploadDto qiNiuUploadDto) {
        logger.info("音乐文件处理结果 qiNiuUploadDto = {}", JSON.toJSON(qiNiuUploadDto));
    }
}
