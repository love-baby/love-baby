package com.love.baby.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author 23770
 */
@EnableEurekaClient
@EnableHystrix
@SpringBootApplication(scanBasePackages={"com.love.baby"})
@EnableFeignClients(basePackages = "com.love.baby.common.api")
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}