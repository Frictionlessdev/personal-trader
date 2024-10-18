package com.sb.projects.trader.DTO.mock;

import com.sb.projects.trader.entity.Strategy;
import com.sb.projects.trader.enums.StrategyStatus;
import com.sb.projects.trader.enums.StrategyType;
import com.sb.projects.trader.repository.StrategyRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;

import java.time.LocalTime;

@AllArgsConstructor
public class MockStrategyDataGenerator {
    private final StrategyRepository strategyRepository;

    @PostConstruct
    void generateStrategy(){
        Strategy strategy = new Strategy();
        strategy.setName("Advanced SIP once per day");
        strategy.setStrategyType(StrategyType.ADVANCED_SIP);
        strategy.setSecurityId("10176");
        strategy.setTotalMonthlyInvestment(70000D);
        strategy.setAllocationPercentage(20D);
        strategy.setTradingDaysInMonth(20);
        strategy.setUserId("shivibh@gmail.com");
        strategy.setStatus(StrategyStatus.Ready);
        strategy.setExpectedPercentChange(-0.25D);
        strategy.setStartTime(LocalTime.of(3, 0));
        strategy.setEndTime(LocalTime.of(22, 30));
        strategy.setRetryCount(1);

        strategyRepository.save(strategy);

        strategy = new Strategy();
        strategy.setName("Advanced SIP once per day");
        strategy.setStrategyType(StrategyType.ADVANCED_SIP);
        strategy.setSecurityId("8506");
        strategy.setTotalMonthlyInvestment(70000D);
        strategy.setAllocationPercentage(20D);
        strategy.setTradingDaysInMonth(20);
        strategy.setUserId("shivibh@gmail.com");
        strategy.setStatus(StrategyStatus.Ready);
        strategy.setExpectedPercentChange(-0.25D);
        strategy.setStartTime(LocalTime.of(3, 0));
        strategy.setEndTime(LocalTime.of(22, 30));
        strategy.setRetryCount(1);

        strategyRepository.save(strategy);

        strategy = new Strategy();
        strategy.setName("Advanced SIP once per day");
        strategy.setStrategyType(StrategyType.ADVANCED_SIP);
        strategy.setSecurityId("14233");
        strategy.setTotalMonthlyInvestment(70000D);
        strategy.setAllocationPercentage(20D);
        strategy.setTradingDaysInMonth(20);
        strategy.setUserId("shivibh@gmail.com");
        strategy.setStatus(StrategyStatus.Ready);
        strategy.setExpectedPercentChange(-0.25D);
        strategy.setStartTime(LocalTime.of(3, 0));
        strategy.setEndTime(LocalTime.of(22, 30));
        strategy.setRetryCount(1);

        strategyRepository.save(strategy);
    }
}
