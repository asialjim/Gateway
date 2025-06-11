package com.asialjim.microapplet.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * 健康检查
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 3.0
 * @since 2025/4/24, &nbsp;&nbsp; <em>version:3.0</em>
 */
@RestController
@RequestMapping
public class IndexController {

    @GetMapping({"/index","/"})
    public String index(){
        return "Welcome to MAMS Gateway";
    }

    @GetMapping("/balance")
    public String balance(){
        return "模拟余额："  + new Random().nextDouble() * 100 + "元";
    }

    @GetMapping("/creditCard")
    public String creditCard(){
        return "模拟信用卡账单:" + new Random().nextDouble() * 100 + "元";
    }

    @GetMapping("/store")
    public String store(){
        return "请上传位置:";
    }
}