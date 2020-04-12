package com.zeny.springcloud.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.zeny.springcloud.dao"})
public class MybatisConfig {
}
