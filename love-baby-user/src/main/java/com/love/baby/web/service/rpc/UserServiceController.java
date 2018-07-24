package com.love.baby.web.service.rpc;

import com.love.baby.web.api.UserRpcService;
import com.love.baby.web.dto.UserDto;
import com.love.baby.web.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangbc on 2018/7/24.
 */
@RestController
@RequestMapping(value = "/user/rpc", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserServiceController implements UserRpcService {

    @Resource
    private UserService userService;

    @Override
    @GetMapping(value = "/listAll", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<UserDto> listAll() {
        List<UserDto> list =new ArrayList<>();
        userService.findAll().forEach(user -> list.add(new UserDto(user)));
        return list;
    }
}
