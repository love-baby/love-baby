package com.love.baby.common.exception;

import com.love.baby.common.util.RenderInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * 异常拦截处理器
 *
 * @author chenmc
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //运行时异常
    @ExceptionHandler(RuntimeException.class)
    public RenderInfo runtimeExceptionHandler(RuntimeException ex) {
        return resultFormat(ex);
    }

    //空指针异常
    @ExceptionHandler(NullPointerException.class)
    public RenderInfo nullPointerExceptionHandler(NullPointerException ex) {
        return resultFormat(ex);
    }

    //类型转换异常
    @ExceptionHandler(ClassCastException.class)
    public RenderInfo classCastExceptionHandler(ClassCastException ex) {
        return resultFormat(ex);
    }

    //IO异常
    @ExceptionHandler(IOException.class)
    public RenderInfo iOExceptionHandler(IOException ex) {
        return resultFormat(ex);
    }

    //未知方法异常
    @ExceptionHandler(NoSuchMethodException.class)
    public RenderInfo noSuchMethodExceptionHandler(NoSuchMethodException ex) {
        return resultFormat(ex);
    }

    //数组越界异常
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public RenderInfo indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException ex) {
        return resultFormat(ex);
    }

    //400错误
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public RenderInfo requestNotReadable(HttpMessageNotReadableException ex) {
        return resultFormat(ex);
    }

    //400错误
    @ExceptionHandler({TypeMismatchException.class})
    public RenderInfo requestTypeMismatch(TypeMismatchException ex) {
        return resultFormat(ex);
    }

    //系统自定义异常
    @ExceptionHandler({SystemException.class})
    public RenderInfo requestStackOverflow(SystemException ex) {
        return resultFormat(ex);
    }

    //其他错误
    @ExceptionHandler({Exception.class})
    public RenderInfo exception(Exception ex) {
        return resultFormat(ex);
    }

    /**
     * 全局错误封装
     *
     * @param ex
     * @param <T>
     * @return
     */
    private <T extends Throwable> RenderInfo resultFormat(T ex) {
        RenderInfo<Object> renderInfo = new RenderInfo<>();
        renderInfo.setCode(500);
        renderInfo.setMessage(ex.getMessage());
        renderInfo.setData(ex.getMessage());
        logger.error("请求失败 = ex", ex);
        return renderInfo;
    }
}
