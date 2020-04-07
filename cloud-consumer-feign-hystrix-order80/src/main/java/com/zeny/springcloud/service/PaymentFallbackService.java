package com.zeny.springcloud.service;

import org.springframework.stereotype.Component;

/**
 * @ClassName PaymentFallbackService
 * @Description TODO
 * @Author zeny
 * @Date 2020/4/5 0005 12:59
 */
@Component
public class PaymentFallbackService implements PaymentHystrixService {
    @Override
    public String paymentInfo_OK(Integer id) {
        return "------PaymentFallbackService fall back  paymentInfo_OK";
    }

    @Override
    public String paymentInfo_Timeout(Integer id) {
        return "------PaymentFallbackService fall back  paymentInfo_Timeout";
    }

    @Override
    public String paymentCircuitBreaker(Integer id) {
        return "------PaymentFallbackService fall back paymentCircuitBreaker";
    }
}
