package com.zeny.springcloud.service.impl;

import com.zeny.springcloud.dao.PaymentDao;
import com.zeny.springcloud.entities.Payment;
import com.zeny.springcloud.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName PaymentServiceImpl
 * @Description TODO
 * @Author zeny
 * @Date 2020/4/2 0002 22:14
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentDao paymentDao;

    /**
     * @Description 新增
     * @Date 2020/4/2 0002 22:16
     * @param payment
     * @return int
     **/
    @Override
    public int create(Payment payment) {
        return paymentDao.create(payment);
    }

    /**
     * @Description 根据ID查询
     * @Date 2020/4/2 0002 22:16
     * @param id
     * @return com.zeny.springcloud.entities.Payment
     **/
    @Override
    public Payment getPaymentById(Long id) {
        return paymentDao.getPaymentById(id);
    }
}
