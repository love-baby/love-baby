package com.love.baby.common.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 前端参数
 *
 * @author liangbc
 * @date 2018/8/25
 */
@Data
@AllArgsConstructor
@Builder
public class SearchParams {
    private String name;
    private String value;

    public static Map findAllParams(SearchParams[] searchParams) {
        Map<String, String> m = new LinkedHashMap<>();
        for (int i = 0; i < searchParams.length; i++) {
            SearchParams searchParam = searchParams[i];
            m.put(searchParam.getName(), searchParam.value);
        }
        return m;
    }
}
