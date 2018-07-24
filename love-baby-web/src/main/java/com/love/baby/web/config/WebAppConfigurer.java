package com.love.baby.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 23770
 */
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ErrorInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/error")
                .excludePathPatterns("/static/*");
    }
}
