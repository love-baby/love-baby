package com.love.baby.web.service.rpc;

import com.love.baby.web.api.UserRpcService;
import com.love.baby.web.bean.User;
import com.love.baby.web.dto.UserDto;
import com.love.baby.web.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
        List<UserDto> list = new ArrayList<>();
        userService.findAll().forEach(user -> list.add(new UserDto(user)));
        return list;
    }

    @Override
    @GetMapping(value = "/findByName", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserDto findByName(@RequestParam(value = "name") String name) {
        User user = userService.findByName(name);
        if (user == null) {
            return null;
        }
        return new UserDto(user);
    }

    @Override
    @PutMapping(value = "/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void create(@RequestBody UserDto userDto) {
        userService.save(new User(userDto));
    }

    @Override
    @GetMapping(value = "/findById", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserDto findById(@RequestParam(value = "id") String id) {
        User user = userService.findById(id);
        if (user == null) {
            return null;
        }
        return new UserDto(user);
    }
}
