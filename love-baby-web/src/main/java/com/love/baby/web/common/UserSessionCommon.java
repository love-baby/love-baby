package com.love.baby.web.common;

import com.google.common.collect.Maps;
import com.love.baby.web.bean.User;
import com.love.baby.web.exception.SystemException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author liangbc
 * @date 2018/7/14
 */
@Component
public class UserSessionCommon {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private Integer USER_SESSION_TIME = 7;

    private String U_TOKENKEY = "userToken:";

    /**
     * 获取用户ID
     *
     * @param token
     * @return
     */
    public String assertSessionAndGetUid(String token) {
        if (StringUtils.isBlank(token)) {
            throw new SystemException(500, "登录失效、请重新登录");
        }
        final HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        String userId = hashOperations.get(U_TOKENKEY + token, "userId");
        if (StringUtils.isBlank(userId)) {
            throw new SystemException(500, "登录失效、请重新登录");
        }
        return userId;
    }

    /**
     * 同步数据到redis
     *
     * @param user
     */
    public Map saveUserToken(User user) {
        final SetOperations<String, String> setOperations = stringRedisTemplate.opsForSet();
        final Set<String> members = setOperations.members(user.getId());


        String token = UUID.randomUUID().toString();
        Map data = Maps.newHashMap();
        data.put("userId", user.getId());
        data.put("token", token);
        data.put("userRole", "user");
        final HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        hashOperations.putAll(U_TOKENKEY + token, data);
        //设置有效期
        stringRedisTemplate.expire(U_TOKENKEY + token, USER_SESSION_TIME, TimeUnit.DAYS);
        return data;
    }

}
