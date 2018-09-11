package com.love.baby.mis.vo;

import com.love.baby.common.dto.UserDto;
import com.love.baby.common.util.StatusUtil;
import com.love.baby.mis.config.SystemConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.*;

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
    private StatusUtil sex;
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
        this.sex = Sex.statusUtil(user.getSex());
        this.avatar = SystemConfig.web_host + user.getAvatar();
        this.createTime = user.getCreateTime();
    }

    public static class Sex {

        public static Integer INITIAL = 0;

        public static Integer MAN = 1;

        public static Integer woman = 2;

        public static StatusUtil statusUtil(Integer sex) {
            List<Map<Integer, String>> listMap = new ArrayList<>();
            Map<Integer, String> m = new HashMap();
            m.put(0, "未知");
            m.put(1, "男");
            m.put(2, "女");
            listMap.add(m);
            StatusUtil statusUtil = StatusUtil.builder().id(sex).statusList(listMap).name("未知").build();
            statusUtil.setName(m.get(sex));
            return statusUtil;
        }
    }
}