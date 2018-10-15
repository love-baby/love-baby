package com.love.baby.common.api;

import com.love.baby.common.annotation.NoWapperResponse;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author liangbc
 * @date 2018/10/15
 */
@FeignClient(name = "love-baby-tool", path = "/conversion/rpc", configuration = ConversionRpcService.MultipartSupportConfig.class)
public interface ConversionRpcService {


    /**
     * wav 转 mp3
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/wavConversionMp3", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @NoWapperResponse
    byte[] wavConversionMp3(@RequestPart(value = "file", required = false) MultipartFile file) throws IOException;


    /**
     * flac 转 mp3
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/flacConversionMp3", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @NoWapperResponse
    byte[] flacConversionMp3(@RequestPart(value = "file", required = false) MultipartFile file) throws IOException;

    /**
     * ape 转 mp3
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/apeConversionMp3", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @NoWapperResponse
    byte[] apeConversionMp3(@RequestPart(value = "file", required = false) MultipartFile file) throws IOException;

    /**
     * RPC 上传文件配置文件
     *
     * @return
     */

    @Configuration
    class MultipartSupportConfig {
        @Bean
        public Encoder feignFormEncoder() {
            return new SpringFormEncoder();
        }
    }
}



