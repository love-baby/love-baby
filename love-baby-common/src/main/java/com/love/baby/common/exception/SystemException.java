package com.love.baby.common.exception;

/**
 * @author liangbc
 * @date 2018/7/14
 */
public class SystemException extends RuntimeException {

    private Integer errorCode;
    private String customMessage;

    public SystemException(Integer errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
        this.customMessage = customMessage;
    }

    public String getCustomMessage() {
        return customMessage;
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}
