package com.zeny.springcloud.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * @ClassName IdGeneratorSnowflake
 * @Description TODO
 * @Author zeny
 * @Date 2020/4/12 0012 18:16
 */
@Component
@Slf4j
public class IdGeneratorSnowflake {

    private long workerId = 0;

    private long datacenterId = 1;

    private Snowflake snowflake = IdUtil.createSnowflake(workerId, datacenterId);

    @PostConstruct
    public void init() {
        try {
            workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
            log.info("当前机器的workerId: {}", workerId);
        }catch (Exception e) {
            e.printStackTrace();
            log.warn("当前机器的workerId获取失败: {}", e.fillInStackTrace());
            workerId = NetUtil.getLocalhostStr().hashCode();
        }

    }

    public synchronized long snowflakeId() {
        return snowflake.nextId();
    }
    public synchronized long snowflakeId(long workerId, long datacenterId) {
        snowflake = IdUtil.createSnowflake(workerId, datacenterId);
        return snowflake.nextId();
    }

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 100; i++) {
            threadPool.submit(()-> {
                long snowflakeId = new IdGeneratorSnowflake().snowflakeId();
                System.out.println(Thread.currentThread().getName() + ":" + snowflakeId);
            });
        }
        threadPool.shutdown();
    }
}
