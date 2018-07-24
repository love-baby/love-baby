package com.love.baby.user.service;

import com.love.baby.user.api.UserRpcService;
import com.love.baby.user.dto.UserDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;

/**
 *
 * @author liangbc
 * @date 2018/6/28
 */
@Service
public class UserService {

    @Resource
    private UserRpcService userRpcService;

    public Flux<UserDto> findAll() {
        return userRpcService.listAll();
    }
}

