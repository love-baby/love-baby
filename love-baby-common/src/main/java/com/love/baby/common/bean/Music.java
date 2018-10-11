package com.love.baby.common.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author liangbc
 * @date 2018/7/4
 */
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class Music {
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
     * 作者
     */
    private String authorId;
    /**
     * 专辑
     */
    private String albumId;
    /**
     * 封面
     */
    private String coverPath;
    /**
     * 歌词地址
     */
    private String lyricsPath;
    /**
     * 描述
     */
    private String description;
    /**
     * 七牛访问连接
     */
    private String qiNiuUrl;
}
