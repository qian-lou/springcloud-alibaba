package com.zeny.springcloud.service;

/**
 * @ClassName PaymentService
 * @Description TODO
 * @Author zeny
 * @Date 2020/4/4 0004 21:43
 */
public interface PaymentService {

    public String paymentInfo(Integer id);
    public String paymentInfoTimeout(Integer id);
    public String paymentCircuitBreaker(Integer id);
}
