package com.love.baby.web.service;

import com.love.baby.web.api.UserRpcService;
import com.love.baby.web.dto.UserDto;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
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
}

