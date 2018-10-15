package com.love.baby.tool.rpc;

import com.love.baby.common.annotation.NoWapperResponse;
import com.love.baby.common.api.ConversionRpcService;
import com.love.baby.tool.service.ConverService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileInputStream;

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
    private ConverService converService;

    @PostMapping(value = "/wavConversionMp3", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @NoWapperResponse
    @Override
    public byte[] wavConversionMp3(@RequestParam(value = "input") FileInputStream input) {
        String path = converService.cacheTemp(input, ".wav");
        return  converService.conversion(path);
    }

    @PostMapping(value = "/flacConversionMp3", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @NoWapperResponse
    @Override
    public byte[] flacConversionMp3(@RequestParam(value = "input") FileInputStream input) {
        String path = converService.cacheTemp(input, ".flac");
        return  converService.conversion(path);
    }

    @PostMapping(value = "/apeConversionMp3", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @NoWapperResponse
    @Override
    public byte[] apeConversionMp3(@RequestParam(value = "input") FileInputStream input) {
        String path = converService.cacheTemp(input, ".ape");
        return  converService.conversion(path);
    }
}
