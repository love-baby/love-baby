package com.love.baby.tool.rpc;

import com.love.baby.common.annotation.NoWapperResponse;
import com.love.baby.common.api.ConversionRpcService;
import com.love.baby.tool.async.AsyncTaskService;
import com.love.baby.tool.service.ConverService;
import feign.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 各种格式转换 RPC
 *
 * @author liangbc
 * @date 2018/10/15
 */
@RestController
@RequestMapping(value = "/conversion/rpc")
public class ConversionRpcController implements ConversionRpcService {

    private static Logger logger = LoggerFactory.getLogger(ConversionRpcService.class);

    @Resource
    private AsyncTaskService asyncTaskService;
    @Resource
    private ConverService converService;

    @PostMapping(value = "/musicConversionMp3", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @NoWapperResponse
    @Override
    public void musicConversionMp3(@Param("file") MultipartFile file) {
        logger.info("处理音乐文件事件");
        asyncTaskService.executeMusicTask(converService.cacheTemp(file));
    }
}
