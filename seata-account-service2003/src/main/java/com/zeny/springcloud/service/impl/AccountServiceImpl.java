package com.zeny.springcloud.service.impl;


import com.zeny.springcloud.dao.AccountDao;
import com.zeny.springcloud.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountDao accountDao;

    @Override
    public void decrease(Long userId, BigDecimal money) {
        log.info("账户扣除余额开始---");
        /*try {
            TimeUnit.SECONDS.sleep(20);
        }catch (Exception e) {
            e.printStackTrace();
        }*/
        accountDao.decrease(userId, money);
        log.info("账户扣除余额结束---");
    }
}
