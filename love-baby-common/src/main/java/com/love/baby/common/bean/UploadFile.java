package com.love.baby.common.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by liangbc on 2018/7/18.
 */
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class UploadFile {
    /**
     * id
     */
    private String id;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 名称
     */
    private String name;
    /**
     * 地址
     */
    private String path;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 文件MD5值
     */
    private String md5;
}
