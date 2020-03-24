package com.juwechat.wechat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *  页面控制器，负责页面的转发
 * @author 秋枫艳梦
 * @date 2019-06-02
 * */
@Controller
public class PageController {

    /**
     *  转发到home.html页面
     * @return 视图名称
     * */
    @RequestMapping(value = "/home.html")
    public String goHome(){
        return "home";
    }
}
