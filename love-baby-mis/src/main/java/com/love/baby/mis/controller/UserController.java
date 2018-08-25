package com.love.baby.mis.controller;

import com.alibaba.fastjson.JSON;
import com.love.baby.common.bean.User;
import com.love.baby.common.common.UserSessionCommon;
import com.love.baby.common.dto.UserDto;
import com.love.baby.common.exception.SystemException;
import com.love.baby.common.util.PageUtil;
import com.love.baby.mis.config.SystemConfig;
import com.love.baby.mis.service.UserService;
import com.love.baby.mis.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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
     * 获取所有用户
     *
     * @return
     */
    @PostMapping("/listAll")
    public PageUtil listAll(@RequestHeader(value = "token") String token, @RequestBody String searchData) {
        String uId = userSessionCommon.assertSessionAndGetUid(token);
        logger.info("获取所有用户 token = {},uId = {},searchData = {}", token, uId, searchData);
        List<UserVo> list = new ArrayList<>();
        userService.findAll().forEach(user -> list.add(new UserVo(user)));
        PageUtil pageUtil = PageUtil.builder()
                .data(list)
                .iTotalDisplayRecords(100)
                .iTotalRecords(1000).build();
        return pageUtil;
    }


    /**
     * 创建用户
     *
     * @param userDto
     * @return
     */
    @PutMapping(value = "")
    public UserDto create(@RequestBody UserDto userDto, @RequestHeader(value = "token") String token) {
        logger.info("创建用户 userDto = {}", JSON.toJSONString(userDto));
        userSessionCommon.assertSessionAndGetUid(token);
        UserDto userDtoOld = userService.findByName(userDto.getName());
        logger.info("userDtoOld = {}", JSON.toJSONString(userDtoOld));
        if (userDtoOld != null) {
            throw new SystemException(500, "用户名已存在");
        }
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        userDto.setId(id);
        userDto.setStatus(User.Status.normal);
        userDto.setResourcesPath(SystemConfig.SystemPath + File.separator + LocalDate.now().getYear() + File.separator + LocalDateTime.now().getMonth().getValue() + File.separator + LocalDateTime.now().getDayOfMonth() + File.separator + id);
        userDto.setCreateTime(Date.from(Instant.now()));
        userService.create(userDto);
        return userDto;
    }


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