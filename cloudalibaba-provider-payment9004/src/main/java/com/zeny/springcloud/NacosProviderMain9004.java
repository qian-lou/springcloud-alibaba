package com.zeny.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName NacosProviderMain9003
 * @Description TODO
 * @Author zeny
 * @Date 2020/4/10 0010 22:20
 */
@SpringBootApplication
@EnableDiscoveryClient
public class NacosProviderMain9004 {
    public static void main(String[] args) {
        SpringApplication.run(NacosProviderMain9004.class, args);
    }
}
