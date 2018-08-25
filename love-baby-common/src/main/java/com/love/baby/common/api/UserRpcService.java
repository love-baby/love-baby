package com.love.baby.common.api;

import com.love.baby.common.dto.UserDto;
import com.love.baby.common.param.SearchUserParams;
import com.love.baby.common.util.PageUtil;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author liangbc
 * @date 2018/7/24
 */
@FeignClient(name = "love-baby-user", path = "/user/rpc")
public interface UserRpcService {

    /**
     * 获取全部用户
     *
     * @return
     */
    @PostMapping(value = "/listAll/{cursor}/{size}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    PageUtil listAll(@PathVariable Integer cursor, @PathVariable Integer size, @RequestBody SearchUserParams searchUserParams);

    /**
     * 用户name获取用户
     *
     * @param name
     * @return
     */
    @GetMapping(value = "/findByName", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    UserDto findByName(@RequestParam(value = "name") String name);

    /**
     * 创建用户
     *
     * @param userDto
     */
    @PutMapping(value = "/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    void create(@RequestBody UserDto userDto);

    /**
     * 根据ID获取用户
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/findById", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    UserDto findById(@RequestParam(value = "id") String id);
}
