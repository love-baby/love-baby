package com.love.baby.common.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangbc
 * @date 2018/8/25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchParamsDto {
    private String dateMin;
    private String dateMax;
    private String searchText;
    private String sort;
    private String sortField;
}
