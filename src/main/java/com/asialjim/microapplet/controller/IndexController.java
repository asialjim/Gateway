package com.asialjim.microapplet.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/index")
    public String index(){
        return "Index";
    }
}