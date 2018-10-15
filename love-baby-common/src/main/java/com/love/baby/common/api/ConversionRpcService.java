package com.love.baby.common.api;

import com.love.baby.common.annotation.NoWapperResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author liangbc
 * @date 2018/10/15
 */
@FeignClient(name = "love-baby-tool", path = "/conversion/rpc")
public interface ConversionRpcService {

    /**
     * wav 转 mp3
     *
     * @param bytes
     * @return
     */
    @PostMapping(value = "/wavConversionMp3", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @NoWapperResponse
    byte[] wavConversionMp3(byte[] bytes);


    /**
     * flac 转 mp3
     *
     * @param bytes
     * @return
     */
    @PostMapping(value = "/flacConversionMp3", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @NoWapperResponse
    byte[] flacConversionMp3(byte[] bytes);

    /**
     * ape 转 mp3
     *
     * @param bytes
     * @return
     */
    @PostMapping(value = "/apeConversionMp3", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @NoWapperResponse
    byte[] apeConversionMp3(byte[] bytes);
}



