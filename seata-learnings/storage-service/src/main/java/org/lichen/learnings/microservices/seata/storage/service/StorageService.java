package org.lichen.learnings.microservices.seata.storage.service;

import org.apache.seata.core.context.RootContext;
import org.lichen.learnings.microservices.seata.storage.mapper.StorageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageService.class);

    @Autowired
    private StorageMapper storageMapper;

    @Transactional(rollbackFor = Exception.class)
    public void decrease(String productId, Integer count) {
        LOGGER.info("================ 库存服务开始扣减 ==================");
        LOGGER.info("当前 XID: {}", RootContext.getXID());

        int result = storageMapper.decrease(productId, count);
        if (result == 0) {
            throw new RuntimeException("库存不足，扣减失败");
        }

        LOGGER.info("库存扣减成功，商品ID: {}, 扣减数量: {}", productId, count);

        if (count > 10) {
            throw new RuntimeException("模拟业务异常: 扣减数量过多");
        }
    }
}
