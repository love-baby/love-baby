package com.love.baby.tool.rpc;

import com.love.baby.common.annotation.NoWapperResponse;
import com.love.baby.common.api.ConversionRpcService;
import com.love.baby.tool.service.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 各种格式转换 RPC
 *
 * @author liangbc
 * @date 2018/10/15
 */
@RestController
@RequestMapping(value = "/conversion/rpc", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ConversionRpcController implements ConversionRpcService {

    @Resource
    private ConversionService conversionService;

    @PostMapping(value = "/wavConversionMp3", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @NoWapperResponse
    @Override
    public byte[] wavConversionMp3(byte[] bytes) {
        String path = conversionService.cacheTemp(bytes, ".wav");
        return  conversionService.conversion(path);
    }

    @PostMapping(value = "/flacConversionMp3", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @NoWapperResponse
    @Override
    public byte[] flacConversionMp3(byte[] bytes) {
        String path = conversionService.cacheTemp(bytes, ".flac");
        return  conversionService.conversion(path);
    }

    @PostMapping(value = "/apeConversionMp3", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @NoWapperResponse
    @Override
    public byte[] apeConversionMp3(byte[] bytes) {
        String path = conversionService.cacheTemp(bytes, ".ape");
        return  conversionService.conversion(path);
    }
}
