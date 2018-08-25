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
public class PageUtil<T> implements Serializable {
    private static final long serialVersionUID = -6718838800112233445L;
    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int number;
    private int size;
    private int numberOfElements;
    private int newJobCount;
}
