package com.love.baby.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 主页
 *
 * @author 23770
 */
@Controller
@RequestMapping("/")
public class IndexController {
    public ModelAndView index() {
        return new ModelAndView("index");
    }
}