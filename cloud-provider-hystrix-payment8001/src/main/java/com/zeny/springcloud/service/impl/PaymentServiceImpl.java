package com.zeny.springcloud.service.impl;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.zeny.springcloud.service.PaymentService;

import org.springframework.cloud.commons.util.IdUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName PaymentServiceImpl
 * @Description TODO
 * @Author zeny
 * @Date 2020/4/4 0004 21:44
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Override
    public String paymentInfo(Integer id) {
        return "线程池: " +Thread.currentThread().getName() + " paymentInfo_OK, id = " + id + "\t" + "O(n_n)O哈哈~";
    }

   /**
    * @Description 服务降级，超时3s就调用fallbackMethod
    * @Date 2020/4/5 0005 11:19
    * @param id
    * @return java.lang.String
    **/
    @HystrixCommand(fallbackMethod = "paymentInfoTimeoutHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
    })
    @Override
    public String paymentInfoTimeout(Integer id) {
//        int a = 10/0;
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池: " +Thread.currentThread().getName() + " paymentInfoTimeout, id = " + id + "\t" + "O(n_n)O哈哈~";
    }

    /**
     * @Description 服务降级调用
     * @Date 2020/4/5 0005 11:19
     * @param id
     * @return java.lang.String
     **/
    public String paymentInfoTimeoutHandler(Integer id) {
        return "线程池: " +Thread.currentThread().getName() + " 系统繁忙，请稍后再试, id = " + id + "\t" + "O(?_?)O呜呜~";
    }



    //服务熔断
    @Override
    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"), //是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"), //请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), //时间窗口期
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60") //失败率到达后跳闸
    })
    public String paymentCircuitBreaker(Integer id) {
        if (id < 0) {
            throw new RuntimeException("*****id 不能是负数");
        }
        String serialNum = IdUtil.simpleUUID();
        return Thread.currentThread().getName() + "\t" + "调用成功， 流水号：" + serialNum;
    }

    public String paymentCircuitBreaker_fallback(Integer id) {
        return "id 不能是负数, 请稍后重试, /T0T/~~ id = " + id;
    }

}
