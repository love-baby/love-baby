package com.love.baby.web.bean;

import com.love.baby.web.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 持久对象
 *
 * @author liangbc
 * @date 2018/6/27
 */
@Data
@AllArgsConstructor
@Builder
public class User {
    /**
     * id
     */
    private String id;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 密码
     */
    private String pwd;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 资源地址
     */
    private String resourcesPath;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;

    public static class Status {
        public static Integer normal = 1;
        public static Integer disabled = 2;
    }

    public User(UserDto userDto) {
        this.id = userDto.getId();
        this.name = userDto.getName();
        this.pwd = userDto.getPwd();
        this.sex = userDto.getSex();
        this.avatar = userDto.getAvatar();
        this.resourcesPath = userDto.getResourcesPath();
        this.status = userDto.getStatus();
        this.createTime = userDto.getCreateTime();
    }


}
