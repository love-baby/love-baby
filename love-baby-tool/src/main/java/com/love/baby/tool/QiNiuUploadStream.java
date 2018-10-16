package com.love.baby.tool;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author liangbc
 * @date 2018/10/16
 */
public interface QiNiuUploadStream {

    String INPUT_QINIU_UPLOAD_ORDER = "inputQiNiuUploadOrder";

    String OUTPUT_QINIU_UPLOAD_ORDER = "outputQiNiuUploadOrder";

    /**
     * 输入
     * @return
     */
    @Input(INPUT_QINIU_UPLOAD_ORDER)
    SubscribableChannel inputQiNiuUploadOrder();

    /**
     * 输出
     *
     * @return
     */
    @Output(OUTPUT_QINIU_UPLOAD_ORDER)
    MessageChannel outputQiNiuUploadOrder();
}
