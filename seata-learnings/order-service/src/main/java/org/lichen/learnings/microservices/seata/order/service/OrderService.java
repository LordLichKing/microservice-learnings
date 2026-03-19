package org.lichen.learnings.microservices.seata.order.service;

import org.apache.seata.core.context.RootContext;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.aspectj.weaver.ast.Or;
import org.lichen.learnings.microservices.seata.order.entity.Order;
import org.lichen.learnings.microservices.seata.order.feign.StorageClient;
import org.lichen.learnings.microservices.seata.order.mapper.OrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private OrderMapper orderMapper;

    private StorageClient storageClient;

    public OrderService(@Autowired OrderMapper orderMapper,
                        @Autowired StorageClient storageClient) {
        this.orderMapper = orderMapper;
        this.storageClient = storageClient;
    }

    @GlobalTransactional(name = "create-order", rollbackFor = Exception.class)
    public void createOrder(String userId, String productId, Integer count) {
        LOGGER.info("==============开始创建订单========================");
        LOGGER.info("当前 XID: {}", RootContext.getXID());

        Order order = new Order();
        order.setUserId(userId);
        order.setProductId(productId);
        order.setCount(count);
        order.setStatus(0);
        orderMapper.insert(order);

        LOGGER.info("订单创建成功，订单ID: {}", order.getId());

//        try {
//            Thread.sleep(60_000L);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        这段时间可以观察数据库里的undo_log数据以及订单状态。

        String result = storageClient.decrease(productId, count);
        LOGGER.info("库存扣减结果：{}", result);

        order.setStatus(1);
        orderMapper.updateById(order);
        LOGGER.info("订单状态更新成功");

        LOGGER.info("===============订单创建完成=====================");
    }
}
