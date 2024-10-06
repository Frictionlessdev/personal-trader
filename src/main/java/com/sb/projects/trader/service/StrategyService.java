package com.sb.projects.trader.service;


import com.sb.projects.trader.entity.Strategy;
import com.sb.projects.trader.entity.StrategyOrder;
import com.sb.projects.trader.enums.StrategyType;

import java.util.List;

public interface StrategyService {
    List<Strategy> getStrategyForUser(String userId, StrategyType strategyType);
}
