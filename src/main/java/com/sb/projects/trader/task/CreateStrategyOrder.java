package com.sb.projects.trader.task;

import com.sb.projects.trader.entity.Strategy;
import com.sb.projects.trader.entity.StrategyOrder;
import com.sb.projects.trader.enums.StrategyType;
import com.sb.projects.trader.service.StrategyOrderService;
import com.sb.projects.trader.service.StrategyService;
import com.sb.projects.trader.service.UserService;
import com.sb.projects.trader.utils.BigDecimalPercentage;
import lombok.AllArgsConstructor;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
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
        try {
            calculateStrategyOrder();
        } catch (Exception e){
            log.error("Unexpected error occured while processing strategy");
        }
    }

    public List<StrategyOrder> calculateStrategyOrder() {
        List<Strategy> strategy = strategyService.getStrategyForUser(userService.getUserId(),
                StrategyType.ADVANCED_SIP);

        return strategy.stream().map(this::calculateStrategyOrderItem).collect(Collectors.toList());
    }

    public StrategyOrder calculateStrategyOrderItem(Strategy strategyItem) {
        Optional<StrategyOrder> strategyOrderItem = strategyOrderService
                .getStrategyOrder(strategyItem.getSecurityId(), strategyItem.getUser());

        BigDecimal bigAggregatedInvestment = new BigDecimal(0);
        if (strategyOrderItem.isPresent()) {
            bigAggregatedInvestment =
                    BigDecimal.valueOf(strategyOrderItem.get().getAggregateInvestment());
        }

        BigDecimal bigTotalMonthlyInvestment = BigDecimal.valueOf(strategyItem.getTotalMonthlyInvestment());
        BigDecimal bigAllocationPercentage = BigDecimal.valueOf(strategyItem.getAllocationPercentage());

        bigAggregatedInvestment.add(BigDecimalPercentage.percentOf(bigTotalMonthlyInvestment, bigAllocationPercentage));

    }
}
