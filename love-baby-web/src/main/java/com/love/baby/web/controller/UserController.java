package com.love.baby.web.controller;

import com.love.baby.web.service.UserService;
import com.love.baby.web.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
    public List<UserVo> listAll(@RequestHeader(value = "token") String token) {
        List<UserVo> list = new ArrayList<>();
        userService.findAll().forEach(user -> list.add(new UserVo(user)));
        return list;
    }
}