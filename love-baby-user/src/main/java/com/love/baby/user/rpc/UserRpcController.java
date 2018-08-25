package com.love.baby.user.rpc;

import com.love.baby.common.api.UserRpcService;
import com.love.baby.common.bean.User;
import com.love.baby.common.dto.UserDto;
import com.love.baby.common.util.PageUtil;
import com.love.baby.user.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liangbc
 * @date 2018/7/24
 */
@RestController
@RequestMapping(value = "/user/rpc", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserRpcController implements UserRpcService {

    @Resource
    private UserService userService;

    @Override
    @GetMapping(value = "/listAll/{cursor}/{size}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PageUtil listAll(@PathVariable Integer cursor, @PathVariable Integer size) {
        List<UserDto> list = new ArrayList<>();
        PageUtil pageUtil = userService.findAll(cursor, size);
        pageUtil.getData().forEach(user -> list.add(new UserDto((User) user)));
        pageUtil.setData(list);
        return pageUtil;
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
