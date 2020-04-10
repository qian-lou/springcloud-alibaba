package com.zeny.springcloud.myhandler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.zeny.springcloud.entities.CommonResult;
import com.zeny.springcloud.entities.Payment;

/**
 * @ClassName CustomerBlockHandler
 * @Description TODO
 * @Author zeny
 * @Date 2020/4/10 0010 18:00
 */
public class CustomerBlockHandler {

    public static CommonResult handlerException(BlockException e) {
        return new CommonResult(4444, "按客户自定义，global handlerException", new Payment(2020L, "seraial003"));
    }

    public static CommonResult handlerException2(BlockException e) {
        return new CommonResult(4444, "按客户自定义，global handlerException2", new Payment(2020L, "seraial004"));
    }


}
