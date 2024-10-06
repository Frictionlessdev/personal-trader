package com.sb.projects.trader.service;

import com.sb.projects.trader.entity.Strategy;
import com.sb.projects.trader.enums.StrategyType;

import java.util.List;

public class StrategyServiceImpl implements StrategyService {
    @Override
    public List<Strategy> getStrategyForUser(String userId, StrategyType strategyType) {
        return List.of();
    }
}
