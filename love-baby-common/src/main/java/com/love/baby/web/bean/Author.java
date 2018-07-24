package com.love.baby.web.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author liangbc
 * @date 2018/7/4
 */
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class Author {
    private String id;
}
