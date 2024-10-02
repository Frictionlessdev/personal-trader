package com.sb.projects.trader.service;

import com.sb.projects.trader.DTO.BrokerErrorDTO;
import com.sb.projects.trader.DTO.OrderDTO;
import com.sb.projects.trader.entity.Order;
import com.sb.projects.trader.enums.OrderStatus;
import com.sb.projects.trader.exceptions.BaseTraderException;

import java.util.List;

public interface OrderService {
    OrderDTO save(OrderDTO order);
    OrderDTO submit(String orderId, OrderStatus status, BrokerErrorDTO brokerErrorDTO) throws BaseTraderException;
    List<Order> getPendingOrders();
    Order get(String orderId) throws BaseTraderException;
}
