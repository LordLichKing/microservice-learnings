package org.lichen.learnings.microservices.seata.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.lichen.learnings.microservices.seata.order.entity.Order;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
