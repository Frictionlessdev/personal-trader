package com.sb.projects.trader.repository;

import com.sb.projects.trader.entity.StrategyOrder;
import com.sb.projects.trader.enums.StrategyType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StrategyOrderRepository extends CrudRepository<StrategyOrder, StrategyOrder> {
    List<StrategyOrder> findByStrategyType(StrategyType strategyType);
    void deleteBySecurityIdAndStrategyType(String securityId, StrategyType strategyType);
}
