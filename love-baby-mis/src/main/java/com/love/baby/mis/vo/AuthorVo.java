package com.love.baby.mis.vo;

import com.love.baby.common.bean.Author;
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
public class AuthorVo {
    private String id;
    private String name;

    public AuthorVo(Author author) {
        this.id = author.getId();
        this.name = author.getName();
    }
}
