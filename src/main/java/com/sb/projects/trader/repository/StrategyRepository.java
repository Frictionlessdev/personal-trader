package com.sb.projects.trader.repository;

import com.sb.projects.trader.entity.Strategy;
import com.sb.projects.trader.enums.StrategyStatus;
import com.sb.projects.trader.enums.StrategyType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StrategyRepository extends CrudRepository<Strategy, Long> {
    List<Strategy> findByUserIdAndStrategyType(String userId, StrategyType strategyType);
    List<Strategy> findByUserIdAndStrategyTypeAndStatus(String userId, StrategyType strategyType, StrategyStatus strategyStatus);
}
