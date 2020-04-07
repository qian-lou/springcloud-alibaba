package com.zeny.springcloud.controller;

import com.zeny.springcloud.entities.CommonResult;
import com.zeny.springcloud.entities.Payment;
import com.zeny.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName PaymentController
 * @Description TODO
 * @Author zeny
 * @Date 2020/4/2 0002 22:16
 */
@RestController
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Value("${server.port}")
    public String serverPort;

    @PostMapping("/payment/create")
    public CommonResult create(@RequestBody Payment payment) {
        int result = paymentService.create(payment);
        log.info("*****插入结果*****: {}, 端口:{}", result, serverPort);
        if (result > 0) {
            return new CommonResult(200, "插入数据库成功, serverPort=" + serverPort, result);
        }else {
            return new CommonResult(444, "插入数据库失败, serverPort=" + serverPort, null);
        }
    }

    @GetMapping("/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id) throws InterruptedException {
        Payment payment = paymentService.getPaymentById(id);
        log.info("*****查询结果*****: {}, 端口:{}", payment, serverPort);
        if (payment != null) {
            return new CommonResult(200, "查询成功, serverPort=" + serverPort, payment);
        }else {
            return new CommonResult(444, "查询 id = "+id+" 失败, serverPort=" + serverPort, null);
        }
    }
}
