package com.zeny.springcloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ConfigClientController
 * @Description TODO
 * @Author zeny
 * @Date 2020/4/6 0006 18:43
 */
@RestController
@RefreshScope
public class ConfigClientController {

    @Value("${config.name}")
    private String name;

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/configInfo")
    public String getName() {
        System.out.println(name);
        return "serverPort: " + serverPort + "\t\n\n configInfo: " + name;
    }
}
