package com.love.baby.web.service;

import com.love.baby.common.api.UserRpcService;
import com.love.baby.common.dto.UserDto;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liangbc
 * @date 2018/6/28
 */
@Service
public class UserService {

    @Resource
    private UserRpcService userRpcService;

    public List<UserDto> findAll() {
        return userRpcService.listAll();
    }

    public UserDto findByName(String name) {
        return userRpcService.findByName(name);
    }

    public void create(UserDto userDto) {
        userRpcService.create(userDto);
    }

    public UserDto findById(String id) {
        return userRpcService.findById(id);
    }

}

