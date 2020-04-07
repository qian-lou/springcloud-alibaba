package com.zeny.springcloud.controller;

import com.netflix.discovery.converters.Auto;
import com.zeny.springcloud.service.IMessageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName SendMessageController
 * @Description TODO
 * @Author zeny
 * @Date 2020/4/7 0007 17:09
 */
@RestController
public class SendMessageController {

    @Autowired
    private IMessageProvider iMessageProvider;

    @GetMapping("/sendMessage")
    public String sendMessage() {
        return iMessageProvider.send();
    }
}
