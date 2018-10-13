package com.love.baby.common.util;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author liangbc
 * @date 2017/8/16
 */
@Data
public class RenderInfo<T> implements Serializable {

    public static final Integer OK = 200;//正常

    public static final Integer ERROR = 500;//默认错误码

    private Integer code;
    private String message;
    private String url;
    private T data;
}
