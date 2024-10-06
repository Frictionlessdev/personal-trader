package com.sb.projects.trader.repository;

import com.sb.projects.trader.entity.Strategy;
import com.sb.projects.trader.enums.StrategyType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StrategyRepository extends CrudRepository<Strategy, Strategy> {
    List<Strategy> findByStrategyType(StrategyType strategyType);
}
