package com.zemy.springcloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName PaymentController
 * @Description TODO
 * @Author zeny
 * @Date 2020/4/8 0008 14:05
 */
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    @RequestMapping("/{id}")
    public String getPayment(@PathVariable("id") Integer id) {
        return "alibaba nacos server " + serverPort + "------" + id;
    }

}
