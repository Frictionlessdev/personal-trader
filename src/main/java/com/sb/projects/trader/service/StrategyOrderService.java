package com.sb.projects.trader.service;

import com.sb.projects.trader.entity.StrategyOrder;
import com.sb.projects.trader.enums.StrategyOrderStatus;
import com.sb.projects.trader.exceptions.BaseTraderException;

import java.util.List;
import java.util.Optional;

public interface StrategyOrderService {
    Optional<StrategyOrder> getStrategyOrder(String securityId, String userId);

    Optional<StrategyOrder> save(StrategyOrder strategyOrder);


    List<StrategyOrder> getStrategyOrders(StrategyOrderStatus strategyOrderStatus);

    StrategyOrder updataStrategyOrderStatus(long strategyOrderId, StrategyOrderStatus strategyOrderStatus) throws BaseTraderException;

    Optional<StrategyOrder> getLastStrategyOrder(String securityId, String userId);
}
