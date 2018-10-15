package com.love.baby.tool.rpc;

import com.love.baby.common.annotation.NoWapperResponse;
import com.love.baby.common.api.ConversionRpcService;
import com.love.baby.tool.service.ConverService;
import feign.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 各种格式转换 RPC
 *
 * @author liangbc
 * @date 2018/10/15
 */
@RestController
@RequestMapping(value = "/conversion/rpc")
public class ConversionRpcController implements ConversionRpcService {

    @Resource
    private ConverService converService;

    @PostMapping(value = "/wavConversionMp3", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @NoWapperResponse
    @Override
    public byte[] wavConversionMp3(@Param("file") MultipartFile file) throws IOException {
        String path = converService.cacheTemp(file.getBytes(), ".wav");
        return converService.conversion(path);
    }

    @PostMapping(value = "/flacConversionMp3", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @NoWapperResponse
    @Override
    public byte[] flacConversionMp3(@Param("file") MultipartFile file) throws IOException {
        String path = converService.cacheTemp(file.getBytes(), ".flac");
        return converService.conversion(path);
    }

    @PostMapping(value = "/apeConversionMp3", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @NoWapperResponse
    @Override
    public byte[] apeConversionMp3(@Param("file") MultipartFile file) throws IOException {
        String path = converService.cacheTemp(file.getBytes(), ".ape");
        return converService.conversion(path);
    }
}
