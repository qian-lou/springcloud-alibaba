package com.zeny.springcloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

/**
 * @ClassName MessageListenerController
 * @Description TODO
 * @Author zeny
 * @Date 2020/4/7 0007 18:18
 */
@Component
@EnableBinding(Sink.class)
public class MessageListenerController {

    @Value("${server.port}")
    private String serverPort;


    @StreamListener(Sink.INPUT)
    public void input(Message<String> message) {
        System.out.println("消费者1号, -----> 接受到的消息: " + message.getPayload() + "\t port: " + serverPort);
    }
}
