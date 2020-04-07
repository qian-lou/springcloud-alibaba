package com.zeny.springcloud.controller;

import com.netflix.discovery.converters.Auto;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.zeny.springcloud.service.PaymentHystrixService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName PaymentHystrixController
 * @Description TODO
 * @Author zeny
 * @Date 2020/4/5 0005 0:35
 */
@RestController
@Slf4j
//@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod")
public class OrderHystrixController {

    @Autowired
    private PaymentHystrixService paymentHystrixService;

    @GetMapping("/consumer/payment/hystrix/ok/{id}")
    public String paymentInfo_ok(@PathVariable("id") Integer id) {
        return paymentHystrixService.paymentInfo_OK(id);
    }

    /**
     * @Description 服务降级
     * @Date 2020/4/5 0005 11:35
     * @param id
     * @return java.lang.String
     **/
    @GetMapping("/consumer/payment/hystrix/timeout/{id}")
//    @HystrixCommand
    public String paymentInfo_Timeout(@PathVariable("id") Integer id) {
        return paymentHystrixService.paymentInfo_Timeout(id);
    }

    /**
     * @Description 服务降级调用
     * @Date 2020/4/5 0005 11:19
     * @param id
     * @return java.lang.String
     **/
//    public String paymentInfoTimeoutHandler(Integer id) {
//        return "我是80 " +Thread.currentThread().getName() + " 系统繁忙，请稍后再试, id = " + id + "\t" + "O(?_?)O呜呜~";
//    }
//
//    public String payment_Global_FallbackMethod() {
//        return "Global 服务错误~~~";
//    }


    //调用 服务熔断
    @GetMapping("/payment/hystrix/cb/{id}")
    public String paymentCircuitBreaker(@PathVariable("id") Integer id) {
        return paymentHystrixService.paymentCircuitBreaker(id);
    }

}
