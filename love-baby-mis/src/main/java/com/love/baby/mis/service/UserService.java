package com.love.baby.mis.service;

import com.love.baby.common.api.UserRpcService;
import com.love.baby.common.dto.UserDto;
import com.love.baby.common.param.SearchUserParams;
import com.love.baby.common.util.PageUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author liangbc
 * @date 2018/6/28
 */
@Service
public class UserService {

    @Resource
    private UserRpcService userRpcService;

    public PageUtil findAll(int cursor, int size, SearchUserParams searchUserParams) {
        return userRpcService.listAll(cursor, size,searchUserParams);
    }

    public UserDto findByName(String name) {
        return userRpcService.findByName(name);
    }

    public void create(UserDto userDto) {
        userRpcService.create(userDto);
    }

    public void update(UserDto userDto) {
        userRpcService.update(userDto);
    }

    public UserDto findById(String id) {
        return userRpcService.findById(id);
    }

}

