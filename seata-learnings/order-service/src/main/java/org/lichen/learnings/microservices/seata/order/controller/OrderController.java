package org.lichen.learnings.microservices.seata.order.controller;

import org.lichen.learnings.microservices.seata.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    // 为了简便直接用@Autowired注解了，建议用constructor注入
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public String create(@RequestParam("userId") String userId, @RequestParam("productId") String productId, @RequestParam("count") Integer count) {

        orderService.createOrder(userId, productId, count);

        return "订单创建成功";
    }
}
