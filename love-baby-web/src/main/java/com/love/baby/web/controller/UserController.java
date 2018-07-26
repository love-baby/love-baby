package com.love.baby.web.controller;

import com.love.baby.common.common.UserSessionCommon;
import com.love.baby.common.dto.UserDto;
import com.love.baby.common.exception.SystemException;
import com.love.baby.web.service.UserService;
import com.love.baby.web.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

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

    @Resource
    private UserSessionCommon userSessionCommon;


    /**
     * 登录
     *
     * @param name
     * @param pwd
     * @return
     */
    @PostMapping(value = "/login")
    public Map login(String name, String pwd) {
        logger.info("登录 name = {},pwd = {}", name, pwd);
        UserDto userDto = userService.findByName(name);
        if (userDto == null) {
            throw new SystemException(500, "登录失败,用户不存在");
        }
        if (!StringUtils.equals(userDto.getPwd(), pwd)) {
            throw new SystemException(500, "登录失败,密码错误");
        }
        //存缓存
        return userSessionCommon.saveUserToken(userDto);
    }


    /**
     * 获取用户信息
     *
     * @param token
     * @return
     */
    @GetMapping("/userInfo")
    public UserVo userInfo(@RequestHeader(value = "token") String token) {
        String userId = userSessionCommon.assertSessionAndGetUid(token);
        UserDto userDto = userService.findById(userId);
        return userDto == null ? null : new UserVo(userDto);
    }
}