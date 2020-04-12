package com.zeny.springcloud.service.impl;


import com.zeny.springcloud.dao.StorageDao;
import com.zeny.springcloud.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class StorageServiceimpl implements StorageService {

    @Resource
    private StorageDao storageDao;


    @Override
    public void decrease(Long productId, Integer count) {
        log.info("库存扣减开始----");
        storageDao.decrease(productId,count);
        log.info("库存扣减结束----");
    }
}
