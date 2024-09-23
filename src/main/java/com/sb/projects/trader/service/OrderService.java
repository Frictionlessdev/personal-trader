package com.sb.projects.trader.service;

import com.sb.projects.trader.DTO.OrderDTO;
import com.sb.projects.trader.entity.Order;
import com.sb.projects.trader.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderDTO save(OrderDTO order);
    OrderDTO submit(String id);
    List<Order> getPendingOrders();
    Order get(String orderId);
    void updateStatus(String id, OrderStatus status);
}
