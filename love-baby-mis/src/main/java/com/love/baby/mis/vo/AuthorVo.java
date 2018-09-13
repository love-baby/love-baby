package com.love.baby.mis.vo;

import com.love.baby.common.bean.Author;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangbc
 * @date 2018/7/4
 */
@Data
@NoArgsConstructor(force = true)
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
