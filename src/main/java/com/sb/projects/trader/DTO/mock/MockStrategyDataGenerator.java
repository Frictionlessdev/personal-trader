package com.sb.projects.trader.DTO.mock;

import com.sb.projects.trader.entity.Strategy;
import com.sb.projects.trader.enums.StrategyType;
import com.sb.projects.trader.repository.StrategyRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MockStrategyDataGenerator {
    private final StrategyRepository strategyRepository;

    @PostConstruct
    void generateStrategy(){
        Strategy strategy = new Strategy();
        strategy.setStrategyType(StrategyType.ADVANCED_SIP);
        strategy.setSecurityId("8506");
        strategy.setTotalMonthlyInvestment(70000D);
        strategy.setAllocationPercentage(20D);

        strategyRepository.save(strategy);
    }
}
