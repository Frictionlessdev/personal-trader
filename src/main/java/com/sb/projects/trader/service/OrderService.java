package com.sb.projects.trader.service;

import com.sb.projects.trader.DTO.OrderDTO;
import com.sb.projects.trader.entity.Order;

import java.util.List;

public interface OrderService {
    OrderDTO save(OrderDTO order);
    OrderDTO submit(String id);
    List<Order> findByStatus(String status);
    Order findById(String id);
}
