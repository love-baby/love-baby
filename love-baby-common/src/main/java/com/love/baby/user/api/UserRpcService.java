package com.love.baby.user.api;

import com.love.baby.user.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;

/**
 *
 * @author liangbc
 * @date 2018/7/24
 */
@FeignClient(name = "love-baby-user", path = "/user/rpc")
public interface UserRpcService {

    @GetMapping(value = "/listAll", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Flux<UserDto> listAll();
}
