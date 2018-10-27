package com.love.baby.common.api;

import com.love.baby.common.annotation.NoWapperResponse;
import com.love.baby.common.config.MultipartSupportConfig;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author liangbc
 * @date 2018/10/15
 */
@FeignClient(name = "love-baby-tool", path = "/conversion/rpc", configuration = MultipartSupportConfig.class)
public interface ConversionRpcService {

    /**
     * wav 转 mp3
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/musicConversionMp3", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @NoWapperResponse
    void musicConversionMp3(@Param("file") MultipartFile file);
}



