package com.sb.projects.trader.service;

import com.sb.projects.trader.entity.StrategyOrder;

import java.util.Optional;

public interface StrategyOrderService {
    Optional<StrategyOrder> getStrategyOrder(String securityId, String userId);
}
