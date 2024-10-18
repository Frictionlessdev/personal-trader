package com.sb.projects.trader.service;


import com.sb.projects.trader.entity.Strategy;
import com.sb.projects.trader.entity.StrategyOrder;
import com.sb.projects.trader.enums.StrategyOrderStatus;
import com.sb.projects.trader.enums.StrategyStatus;
import com.sb.projects.trader.enums.StrategyType;
import com.sb.projects.trader.exceptions.BaseTraderException;

import java.util.List;
import java.util.Optional;

public interface StrategyService {
    List<Strategy> getStrategyForUser(String userId, StrategyType strategyType) throws BaseTraderException;

    List<StrategyOrder> getPendingStrategyOrders() throws BaseTraderException;

    Optional<StrategyOrder> updateStrategyOrderStatus(long strategyOrderId, StrategyOrderStatus strategyOrderStatus) throws BaseTraderException;

    Optional<Strategy> updateStrategyStatus(long strategyId, StrategyStatus previous, StrategyStatus strategyStatus) throws BaseTraderException;

    Optional<Strategy> updateAggregatedInvestment(long strategyId, double aggregatedInvestment);

    Optional<Strategy> getStrategy(long strategyId);
}
