package com.zeny.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName ApplicationContextConfig
 * @Description TODO
 * @Author zeny
 * @Date 2020/4/2 0002 23:51
 */
@Configuration
public class ApplicationContextConfig {

    @Bean
    @LoadBalanced  //负载均衡
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
