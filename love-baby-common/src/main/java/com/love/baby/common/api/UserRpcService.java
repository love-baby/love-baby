package com.love.baby.common.api;

import com.love.baby.common.annotation.NoWapperResponse;
import com.love.baby.common.common.bean.PageUtil;
import com.love.baby.common.config.MultipartSupportConfig;
import com.love.baby.common.dto.UserDto;
import com.love.baby.common.param.SearchParamsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author liangbc
 * @date 2018/7/24
 */
@FeignClient(name = "love-baby-user", path = "/user/rpc", configuration = MultipartSupportConfig.class)
public interface UserRpcService {

    /**
     * 获取全部用户
     *
     * @return
     */
    @PostMapping(value = "/listAll/{cursor}/{size}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @NoWapperResponse
    PageUtil listAll(@PathVariable Integer cursor, @PathVariable Integer size, @RequestBody SearchParamsDto searchParamsDto);

    /**
     * 用户name获取用户
     *
     * @param name
     * @return
     */
    @GetMapping(value = "/findByName", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @NoWapperResponse
    UserDto findByName(@RequestParam(value = "name") String name);

    /**
     * 创建用户
     *
     * @param userDto
     */
    @PutMapping(value = "/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @NoWapperResponse
    UserDto create(@RequestBody UserDto userDto);

    /**
     * 根据ID获取用户
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/findById", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @NoWapperResponse
    UserDto findById(@RequestParam(value = "id") String id);

    /**
     * 修改
     *
     * @param userDto
     * @return
     */
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @NoWapperResponse
    UserDto update(@RequestBody UserDto userDto);

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @NoWapperResponse
    void delete(@RequestParam(value = "id") String id);
}
