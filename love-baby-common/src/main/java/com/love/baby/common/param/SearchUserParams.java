package com.love.baby.common.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author liangbc
 * @date 2018/8/25
 */
@Data
@AllArgsConstructor
@Builder
public class SearchUserParams {
    private String dateMin;
    private String dateMax;
    private String searchText;
    private String sort;
    private String sortField;
}
