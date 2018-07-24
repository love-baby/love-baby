package com.love.baby.user.dto;

import com.love.baby.user.bean.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 服务之间传输的对象
 *
 * @author liangbc
 * @date 2018/6/28
 */
@Data
@AllArgsConstructor
@Builder
public class UserDto {
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


    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.pwd = user.getPwd();
        this.sex = user.getSex();
        this.avatar = user.getAvatar();
        this.resourcesPath = user.getResourcesPath();
        this.status = user.getStatus();
        this.createTime = user.getCreateTime();
    }
}