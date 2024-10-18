package com.sb.projects.trader.task;

import com.sb.projects.trader.entity.Strategy;
import com.sb.projects.trader.entity.StrategyOrder;
import com.sb.projects.trader.enums.*;
import com.sb.projects.trader.service.StrategyOrderService;
import com.sb.projects.trader.service.StrategyService;
import com.sb.projects.trader.service.UserService;
import com.sb.projects.trader.utils.BigDecimalPercentage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class CreateStrategyOrder implements Runnable {

    private final StrategyService strategyService;
    private final StrategyOrderService strategyOrderService;
    private final UserService userService;

    @Override
    public void run() {
        log.info("Running create strategy order task...");
        try {
            calculateStrategyOrder();
        } catch (Exception e){
            log.error("Unexpected error occurred while processing strategy", e);
        }
    }

    public void calculateStrategyOrder() {
        List<Strategy> strategy = strategyService.getStrategyForUser(userService.getUserId(),
                StrategyType.ADVANCED_SIP);

        if (strategy.isEmpty()) {
            log.error("No strategy available for user id: '{}'", userService.getUserId());
        }

        strategy.stream()
                .filter(this::shouldExecuteStrategy)
                .forEach(this::calculateStrategyOrder);
    }

    /*
        1. Get status of previous day strategy order.
        2. if status is success then ignore this and create new strategy order based on strategy
            2a. add daily investment to monthly aggregate, if less create, else dont create
        3. if status is failed then pick the investment amount and create new strategy order based on strategy
            (add previous day failed investment)
            3a. add monthly aggregate + daily investment + previous pending, if limit not breached then create,
            else create with only previous + monthly aggregate
    */
    public void calculateStrategyOrder(Strategy strategyItem) {

        log.info("Processing strategy: {}", strategyItem);
        var bigTotalMonthlyInvestment = BigDecimal.valueOf(strategyItem.getTotalMonthlyInvestment());
        var bigAllocationPercentage = BigDecimal.valueOf(strategyItem.getAllocationPercentage());
        var bigMonthlyInvestment = BigDecimalPercentage.percentOf(bigAllocationPercentage, bigTotalMonthlyInvestment);
        var dailyInvestment = bigMonthlyInvestment.divide(BigDecimal.valueOf(strategyItem.getTradingDaysInMonth()), RoundingMode.FLOOR);
        final var bigMonthlyAggregatedInvestment = BigDecimal.valueOf(strategyItem.getAggregateInvestment()).add(dailyInvestment);

        var strategyOrderItem = strategyOrderService.getLastStrategyOrder(strategyItem.getSecurityId(),
                strategyItem.getUserId());

        strategyOrderItem.ifPresentOrElse(s -> {

            BigDecimal bigAggregatedInvestment = dailyInvestment;
            if (s.getStatus() == StrategyOrderStatus.Rejected || s.getStatus() == StrategyOrderStatus.Failed) {
                log.info("Found previous pending strategy order: '{}'", s);
                bigAggregatedInvestment = bigAggregatedInvestment.add(BigDecimal.valueOf(s.getInvestment()));
            }

            if (bigMonthlyInvestment.compareTo(bigAggregatedInvestment.add(bigMonthlyAggregatedInvestment)) < 0) {
                log.error("Strategy monthly limit breached: '{}'", s);
                if (s.getStatus() == StrategyOrderStatus.Rejected || s.getStatus() == StrategyOrderStatus.Failed)
                        bigAggregatedInvestment = BigDecimal.valueOf(s.getInvestment());
                else return;
            }
            strategyService.updateStrategyStatus(strategyItem.getId(), StrategyStatus.Ready, StrategyStatus.Processing);

            var newStrategyOrderItem = getStrategyOrder(strategyItem, bigAggregatedInvestment.doubleValue());
            strategyOrderService.save(newStrategyOrderItem).ifPresentOrElse(
                    so -> {
                        log.info("Successfully created strategy order: '{}'", so);
                        strategyService.updateStrategyStatus(strategyItem.getId(), StrategyStatus.Ready, StrategyStatus.Processed);
                    },
                    () -> {
                        log.error("Error saving strategy order for strategy id: '{}'", strategyItem.getId());
                        strategyService.updateStrategyStatus(strategyItem.getId(), StrategyStatus.Ready, StrategyStatus.Failed);
                    });
        }, () -> {

            var newStrategyOrderItem = getStrategyOrder(strategyItem, dailyInvestment.doubleValue());
            strategyOrderService.save(newStrategyOrderItem).ifPresentOrElse(
                    so -> {
                        log.info("Successfully created first strategy order: '{}'", so);
                        strategyService.updateStrategyStatus(strategyItem.getId(), StrategyStatus.Ready, StrategyStatus.Processed);
                    },
                    () -> {
                        log.error("Error saving first strategy order for strategy id: '{}'", strategyItem.getId());
                        strategyService.updateStrategyStatus(strategyItem.getId(), StrategyStatus.Ready, StrategyStatus.Failed);
                    });

        });
    }

    private static @NotNull StrategyOrder getStrategyOrder(Strategy strategyItem, double aggregatedInvestment) {
        var newStrategyOrderItem = new StrategyOrder();
        newStrategyOrderItem.setStrategyId(strategyItem.getId());
        newStrategyOrderItem.setStrategyType(strategyItem.getStrategyType());
        newStrategyOrderItem.setOrderPrice(0.0D);
        newStrategyOrderItem.setSecurityId(strategyItem.getSecurityId());
        newStrategyOrderItem.setInvestment(aggregatedInvestment);
        newStrategyOrderItem.setUserId(strategyItem.getUserId());
        newStrategyOrderItem.setCurrentMktPrice(0.0D); //TODO
        newStrategyOrderItem.setExchange(Exchange.NSE);
        newStrategyOrderItem.setInstrumentType(InstrumentType.ETF);
        newStrategyOrderItem.setStatus(StrategyOrderStatus.Ready);
        newStrategyOrderItem.setPercentChange(strategyItem.getExpectedPercentChange());
        return newStrategyOrderItem;
    }

    private boolean shouldExecuteStrategy(Strategy strategyItem) {
        boolean result = LocalTime.now(ZoneId.of("Asia/Kolkata")).isAfter(strategyItem.getStartTime()) &&
                LocalTime.now(ZoneId.of("Asia/Kolkata")).isBefore(strategyItem.getEndTime()) &&
                strategyItem.getRetryCount() > 0;

        log.info("Strategy item: '{}' should be executed: '{}'", strategyItem.getName(), result);

        return result;
    }
}
