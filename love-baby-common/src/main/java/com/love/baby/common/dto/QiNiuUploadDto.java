package com.love.baby.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangbc
 * @date 2018/10/16
 */
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class QiNiuUploadDto {
    /**
     * 状态码
     */
    private int code;
    /**
     * 消息
     */
    private String message;
    /**
     * 文件MD5
     */
    private String md5;
    /**
     * 七牛路径
     */
    private String qiNiuUrl;

    public static class Code {
        public static final int OK = 200;
        public static final int ERROR = 500;
    }

}
