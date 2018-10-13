package com.love.baby.mis.common;

import com.alibaba.fastjson.JSON;
import com.love.baby.common.annotation.NoWapperResponse;
import com.love.baby.common.util.RenderInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * 全局数据格式返回拦截器
 *
 * @author 23770
 */
@RestControllerAdvice(basePackages = "com.love.baby.mis.controller")
public class GlobalHandler implements ResponseBodyAdvice<Object> {

    private static Logger logger = LoggerFactory.getLogger(GlobalHandler.class);

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    /**
     * 全局正常数据返回拦截
     *
     * @param body
     * @param returnType
     * @param selectedContentType
     * @param selectedConverterType
     * @param request
     * @param response
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        logger.info("全局统一封装 path = {},body = {}", request.getURI().getPath(), JSON.toJSONString(body));
        //不需要进行结果包装
        if (Objects.nonNull(returnType.getMethodAnnotation(NoWapperResponse.class))) {
            return body;
        }
        RenderInfo<Object> renderInfo = new RenderInfo<>();
        if (body instanceof RenderInfo) {
            renderInfo = JSON.parseObject(JSON.toJSONString(body), RenderInfo.class);
            if (StringUtils.isBlank(renderInfo.getUrl())) {
                renderInfo.setUrl(request.getURI().getPath());
            }
            return renderInfo;
        }
        renderInfo.setUrl(request.getURI().getPath());
        renderInfo.setCode(RenderInfo.OK);
        renderInfo.setMessage("请求成功");
        renderInfo.setData(body);
        return renderInfo;
    }
}