package com.love.baby.web.api;

import com.love.baby.web.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

/**
 * @author liangbc
 * @date 2018/7/24
 */
@FeignClient(name = "love-baby-user", path = "/user/rpc")
public interface UserRpcService {

    @GetMapping(value = "/listAll", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    List<UserDto> listAll();

    @GetMapping(value = "/findByName", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    UserDto findByName(String name);

    @PutMapping(value = "/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    void create(UserDto userDto);

    @GetMapping(value = "/findById", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    UserDto findById(String id);
}
