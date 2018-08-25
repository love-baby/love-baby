package com.love.baby.common.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liangbc on 16/8/11.
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
