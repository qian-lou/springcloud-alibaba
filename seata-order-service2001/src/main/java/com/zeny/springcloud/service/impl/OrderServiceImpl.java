package com.zeny.springcloud.service.impl;


import com.zeny.springcloud.dao.OrderDao;
import com.zeny.springcloud.domain.Order;
import com.zeny.springcloud.service.AccountService;
import com.zeny.springcloud.service.OrderService;
import com.zeny.springcloud.service.StorageService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;
    @Resource
    private StorageService storageService;
    @Resource
    private AccountService accountService;


    @GlobalTransactional(name = "fsp-create-order", rollbackFor = Exception.class)
    @Override
    public void create(Order order) {
        log.info("-------->开始创建新订单");
        orderDao.create(order);
        log.info("--------订单微服务开始调用库存,做扣减");
        storageService.decrease(order.getProductId(), order.getCount());
        log.info("-------订单微服务开始调用库存，做扣减end");

        log.info("-------订单微服务开始调用账户，做扣减");
        accountService.decrease(order.getUserId(), order.getMoney());
        log.info("-------订单微服务开始调用账户，做扣减end");
        log.info("-------修改订单状态");
        orderDao.update(order.getUserId(), 0);
        log.info("-------修改订单状态结束");

        log.info("--------下订单结束了，哈哈哈哈");
    }
}
