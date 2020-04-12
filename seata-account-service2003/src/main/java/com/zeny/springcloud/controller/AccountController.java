package com.zeny.springcloud.controller;


import com.zeny.springcloud.entities.CommonResult;
import com.zeny.springcloud.service.AccountService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

@RestController
@RequestMapping(value = "account")
public class AccountController {

    @Resource
    private AccountService accountService;

    @PostMapping(value = "decrease")
    public CommonResult decrease(@RequestParam("userId") Long userId,
                                 @RequestParam("money")BigDecimal money) {
        accountService.decrease(userId, money);
        return new CommonResult(200,"账户余额扣减成功，哈哈哈");
    }
}
