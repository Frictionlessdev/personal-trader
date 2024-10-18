package com.sb.projects.trader.task;

import com.sb.projects.trader.entity.Strategy;
import com.sb.projects.trader.enums.StrategyType;
import com.sb.projects.trader.service.StrategyService;
import com.sb.projects.trader.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class CreateStrategy implements Runnable{

    private final StrategyService strategyService;
    private final UserService userService;
    private final List<Strategy> strategies;

    @Override
    public void run() {
        try {
            createStrategies();
        } catch (Exception ex){
            log.error("Unknown error occured while creating strategy {}", ex);
        }
    }

    private void createStrategies() {
        strategies.stream().forEach(s -> {
            var strategyItem = strategyService.getStrategyForUser(userService.getUserId(),
                    StrategyType.ADVANCED_SIP);
        });
    }
}
