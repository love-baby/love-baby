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

    /**
     * 输入
     *
     * @return
     */
    @Input("music_conversion")
    SubscribableChannel musicConversionInputChannel();

    /**
     * 输出
     *
     * @return
     */
    @Output("music_conversion")
    MessageChannel musicConversionOutputChannel();
}
