package com.zeny.springcloud.service;

import com.zeny.springcloud.entities.Payment;
import org.apache.ibatis.annotations.Param;

public interface PaymentService {
    int create(Payment payment);

    Payment getPaymentById(Long id);
}
