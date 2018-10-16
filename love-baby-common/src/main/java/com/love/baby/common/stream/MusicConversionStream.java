package com.love.baby.common.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author liangbc
 * @date 2018/10/16
 */
public interface MusicConversionStream {

    String MUSIC_CONVERSION_INPUT = "musicConversionInput";

    String MUSIC_CONVERSION_OUTPUT = "musicConversionOutput";

    /**
     * 输入
     *
     * @return
     */
    @Input(MUSIC_CONVERSION_INPUT)
    SubscribableChannel musicConversionInput();

    /**
     * 输出
     *
     * @return
     */
    @Output(MUSIC_CONVERSION_OUTPUT)
    MessageChannel musicConversionOutput();
}
