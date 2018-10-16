package com.love.baby.common.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author liangbc
 * @date 2018/10/16
 */
public interface MusicConversionStream {

    String MUSIC_CONVERSION_INPUT = "musicConversionExchange";

    String MUSIC_CONVERSION_OUTPUT = "musicConversionExchange";


    /**
     * 输出
     *
     * @return
     */
    @Output(MUSIC_CONVERSION_OUTPUT)
    MessageChannel musicConversionOutput();
}
