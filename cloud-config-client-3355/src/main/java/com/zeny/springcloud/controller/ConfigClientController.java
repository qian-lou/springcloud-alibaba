package com.zeny.springcloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ConfigClientController
 * @Description TODO
 * @Author zeny
 * @Date 2020/4/6 0006 18:43
 */
@RestController
public class ConfigClientController {

    @Value("${config.name}")
    private String name;

    @GetMapping("/configInfo")
    public String getName() {
        System.out.println(name);
        return name;
    }
}
