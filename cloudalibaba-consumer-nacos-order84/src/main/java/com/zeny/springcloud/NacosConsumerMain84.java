package com.zeny.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName NacosConsumerMain84
 * @Description TODO
 * @Author zeny
 * @Date 2020/4/10 0010 22:42
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class NacosConsumerMain84 {
    public static void main(String[] args) {
        SpringApplication.run(NacosConsumerMain84.class, args);
    }
}
