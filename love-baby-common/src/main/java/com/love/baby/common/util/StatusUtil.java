package com.love.baby.common.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author liangbc
 * @date 2018/8/29
 */
@Data
@AllArgsConstructor
@Builder
public class StatusUtil {
    /**
     * 后端数据库存的数据
     */
    private Integer id;
    /**
     * 前端显示的数据
     */
    private String name;
    /**
     * 当前字段所有状态
     */
    private List<Map<Integer, String>> statusList;
}
