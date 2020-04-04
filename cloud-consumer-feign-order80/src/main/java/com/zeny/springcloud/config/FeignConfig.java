package com.zeny.springcloud.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName FeignConfig
 * @Description TODO
 * @Author zeny
 * @Date 2020/4/4 0004 18:41
 */
@Configuration
public class FeignConfig {

    @Bean
    public Logger.Level logger() {
        return Logger.Level.FULL;
    }

}
