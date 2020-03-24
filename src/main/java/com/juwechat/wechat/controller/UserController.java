package com.juwechat.wechat.controller;

import com.alibaba.fastjson.JSON;
import com.juwechat.wechat.utils.UserInfoUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/user",produces = {"application/json;charset=utf-8"})
@ResponseBody
public class UserController {

    @RequestMapping(value = "/get/info/{code}")
    public Object getUserInfo(@PathVariable String code){
        return JSON.parseObject(UserInfoUtil.getInfoByCode(code));
    }
}
