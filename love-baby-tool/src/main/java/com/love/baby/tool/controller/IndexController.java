package com.love.baby.tool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * 主页
 *
 * @author 23770
 */
@Controller
@RequestMapping("/")
public class IndexController {

    public Map index() {
        Map m = new HashMap();
        m.put("a","a");
        m.put("b","b");
        m.put("c","c");
        return m;
    }
}