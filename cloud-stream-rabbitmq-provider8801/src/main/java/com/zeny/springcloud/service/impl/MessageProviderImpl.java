package com.zeny.springcloud.service.impl;

import com.zeny.springcloud.service.IMessageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;


import java.util.UUID;

/**
 * @ClassName MessageProviderImpl
 * @Description TODO
 * @Author zeny
 * @Date 2020/4/7 0007 16:39
 */
@EnableBinding(Source.class) //定义消息的推送管道,指channel和exchange绑定在一起
public class MessageProviderImpl implements IMessageProvider {

    //消息发送管道
    @Autowired
    private MessageChannel output;

    @Override
    public String send() {
        String serial = UUID.randomUUID().toString();
        output.send(MessageBuilder.withPayload(serial).build());
        System.out.println("*****serial: " + serial);
        return null;
    }
}
