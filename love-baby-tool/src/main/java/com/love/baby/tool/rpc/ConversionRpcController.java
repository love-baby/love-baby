package com.love.baby.tool.rpc;

import com.love.baby.common.annotation.NoWapperResponse;
import com.love.baby.common.api.ConversionRpcService;
import com.love.baby.tool.service.ConverService;
import feign.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @Resource
    private ConverService converService;

    @PostMapping(value = "/wavConversionMp3", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @NoWapperResponse
    @Override
    public ResponseEntity<byte[]> wavConversionMp3(@Param("file") MultipartFile file) {
        String path = converService.cacheTemp(file);
        byte[] bytes = converService.conversion(path);
        return responseEntity(bytes);
    }

    @PostMapping(value = "/flacConversionMp3", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @NoWapperResponse
    @Override
    public ResponseEntity<byte[]> flacConversionMp3(@Param("file") MultipartFile file){
        String path = converService.cacheTemp(file);
        byte[] bytes = converService.conversion(path);
        return responseEntity(bytes);
    }

    @PostMapping(value = "/apeConversionMp3", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @NoWapperResponse
    @Override
    public ResponseEntity<byte[]> apeConversionMp3(@Param("file") MultipartFile file){
        String path = converService.cacheTemp(file);
        byte[] bytes = converService.conversion(path);
        return responseEntity(bytes);
    }

    private ResponseEntity<byte[]> responseEntity(byte[] bytes){
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(bytes, headers, status);
    }

}
