package com.zeny.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @ClassName Order80
 * @Description TODO
 * @Author zeny
 * @Date 2020/4/2 0002 23:45
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class OrderMain80 {
    public static void main(String[] args) {
        SpringApplication.run(OrderMain80.class, args);
    }
}
