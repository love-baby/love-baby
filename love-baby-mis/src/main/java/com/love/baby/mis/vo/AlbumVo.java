package com.love.baby.mis.vo;

import com.love.baby.common.bean.Album;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author liangbc
 * @date 2018/7/4
 */
@Data
@AllArgsConstructor
@Builder
public class AlbumVo {
    private String id;
    private String name;

    public AlbumVo(Album album) {
        this.id = album.getId();
        this.name = album.getName();
    }
}
