package com.love.baby.common.common.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author liangbc
 * @date 16/8/11
 */
@Data
@AllArgsConstructor
@Builder
public class PageUtil implements Serializable {
    private int sEcho;
    private List data;
    private int recordsFiltered;
    private int recordsTotal;
}
