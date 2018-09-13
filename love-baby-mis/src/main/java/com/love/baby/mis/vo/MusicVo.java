package com.love.baby.mis.vo;

import com.love.baby.common.bean.Album;
import com.love.baby.common.bean.Author;
import com.love.baby.common.bean.Music;
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
public class MusicVo {
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
    private AuthorVo author;
    /**
     * 专辑
     */
    private AlbumVo album;
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

    public MusicVo(Author author, Album album, Music music) {
        this.id = music.getId();
        this.createTime = music.getCreateTime();
        this.name = music.getName();
        this.path = music.getPath();
        this.coverPath = music.getCoverPath();
        this.lyricsPath = music.getLyricsPath();
        this.author = new AuthorVo(author);
        this.album = new AlbumVo(album);
    }
}
