package com.love.baby.web.api;

import com.love.baby.web.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
    @GetMapping(value = "/listAll", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    List<UserDto> listAll();

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
