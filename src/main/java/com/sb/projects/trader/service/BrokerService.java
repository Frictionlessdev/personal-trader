package com.sb.projects.trader.service;

import com.sb.projects.trader.entity.Order;

public interface BrokerService {
    void submitOrder(Order order);
}
