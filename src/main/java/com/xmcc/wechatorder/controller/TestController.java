package com.xmcc.wechatorder.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/hello")
    public String hello(){
        System.out.println("aaaaaa");
        return "hello spring-boot";
    }

}
