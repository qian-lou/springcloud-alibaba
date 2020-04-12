package com.zeny.springcloud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName HutoolController
 * @Description TODO
 * @Author zeny
 * @Date 2020/4/12 0012 18:15
 */
@RestController
public class HutoolController {

    @GetMapping("/get")
    public String getHutool() {
        return "";
    }
}
