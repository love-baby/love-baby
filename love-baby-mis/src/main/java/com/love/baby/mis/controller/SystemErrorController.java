package com.love.baby.mis.controller;

import com.alibaba.fastjson.JSON;
import com.love.baby.common.util.RenderInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局的404
 *
 * @author liangbc
 * @date 2018/7/12
 */
@RestController
public class SystemErrorController implements ErrorController {

    private static Logger logger = LoggerFactory.getLogger(SystemErrorController.class);

    private static final String ERROR_PATH = "/error";

    @RequestMapping(value = ERROR_PATH)
    public RenderInfo handleError(HttpServletRequest request) {
        RenderInfo renderInfo = buildRenderInfo(request);
        logger.error("资源不存在 404 = {}", JSON.toJSONString(renderInfo));
        return renderInfo;
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    private RenderInfo buildRenderInfo(HttpServletRequest request) {
        RenderInfo<Object> renderInfo = new RenderInfo<>();
        Object status = request.getAttribute("javax.servlet.error.status_code");
        Object message = request.getAttribute("javax.servlet.error.message");
        Object request_uri = request.getAttribute("javax.servlet.forward.request_uri");
        renderInfo.setCode(Integer.parseInt(String.valueOf(status)));
        renderInfo.setMessage(String.valueOf(message));
        renderInfo.setUrl(String.valueOf(request_uri));
        renderInfo.setData("请求失败");
        return renderInfo;
    }
}
