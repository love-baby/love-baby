package com.love.baby.user.controller.rpc;

import com.love.baby.user.api.UserRpcService;
import com.love.baby.user.dto.UserDto;
import com.love.baby.user.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;

/**
 * Created by liangbc on 2018/7/24.
 */
@RestController
@RequestMapping(value = "/user/rpc", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserRpcController implements UserRpcService {

    @Resource
    private UserService userService;

    @Override
    @GetMapping(value = "/listAll", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Flux<UserDto> listAll() {
        return Flux.create(userFluxSink -> {
            userService.findAll().forEach(user -> userFluxSink.next(new UserDto(user)));
            userFluxSink.complete();
        });

    }
}
