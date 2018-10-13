package com.love.baby.tool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 23770
 */
@EnableEurekaClient
@EnableHystrix
@SpringBootApplication(scanBasePackages={"com.love.baby"})
@EnableFeignClients(basePackages = "com.love.baby.common.api")
@RestControllerAdvice(basePackages = "com.love.baby.tool.controller")
public class ToolApplication {
    public static void main(String[] args) {
        SpringApplication.run(ToolApplication.class, args);
    }
}