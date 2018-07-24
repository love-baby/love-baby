package com.love.baby.web.controller;

import com.love.baby.web.service.UserService;
import com.love.baby.web.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;

/**
 * @author liangbc
 * @date 2017/8/8
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    /**
     * 获取所有用户
     *
     * @return
     */
    @GetMapping("/listAll")
    public Flux<UserVo> listAll(@RequestHeader(value = "token") String token) {
        return Flux.create(userFluxSink -> {
            userService.findAll().toStream().forEach(user -> userFluxSink.next(new UserVo(user)));
            userFluxSink.complete();
        });
    }
}