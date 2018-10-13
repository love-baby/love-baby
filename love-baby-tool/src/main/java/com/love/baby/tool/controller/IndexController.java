package com.love.baby.tool.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 主页
 *
 * @author 23770
 */
@RestController
@RequestMapping("/index")
public class IndexController {

    @GetMapping(value = "")
    public Map index() {
        Map m = new HashMap();
        m.put("a","a");
        m.put("b","b");
        m.put("c","c");
        return m;
    }
}