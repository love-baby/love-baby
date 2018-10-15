package com.love.baby.common.api;

import com.love.baby.common.annotation.NoWapperResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileInputStream;

/**
 * @author liangbc
 * @date 2018/10/15
 */
@FeignClient(name = "love-baby-tool", path = "/conversion/rpc")
public interface ConversionRpcService {

    /**
     * wav 转 mp3
     *
     * @param input
     * @return
     */
    @PostMapping(value = "/wavConversionMp3", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    @NoWapperResponse
    byte[] wavConversionMp3(@RequestParam FileInputStream input);


    /**
     * flac 转 mp3
     *
     * @param input
     * @return
     */
    @PostMapping(value = "/flacConversionMp3", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    @NoWapperResponse
    byte[] flacConversionMp3(@RequestParam FileInputStream input);

    /**
     * ape 转 mp3
     *
     * @param input
     * @return
     */
    @PostMapping(value = "/apeConversionMp3", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    @NoWapperResponse
    byte[] apeConversionMp3(@RequestParam FileInputStream input);
}



