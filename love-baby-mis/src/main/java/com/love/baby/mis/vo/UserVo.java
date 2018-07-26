package com.love.baby.mis.vo;

import com.love.baby.common.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 展示层显示的对象
 *
 * @author liangbc
 * @date 2018/6/28
 */
@Data
@AllArgsConstructor
@Builder
public class UserVo {
    /**
     * id
     */
    private String id;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 创建时间
     */
    private Date createTime;


    public UserVo(UserDto user) {
        this.id = user.getId();
        this.name = user.getName();
        this.sex = user.getSex();
        this.avatar = user.getAvatar();
        this.createTime = user.getCreateTime();
    }
}