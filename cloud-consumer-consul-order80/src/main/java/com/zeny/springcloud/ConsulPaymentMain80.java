package com.zeny.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName PaymentMain8006
 * @Description TODO
 * @Author zeny
 * @Date 2020/4/3 0003 21:38
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ConsulPaymentMain80 {
    public static void main(String[] args) {
        SpringApplication.run(ConsulPaymentMain80.class, args);
    }
}
