package com.sb.projects.trader.service;

import com.sb.projects.trader.entity.Strategy;
import com.sb.projects.trader.entity.StrategyOrder;
import com.sb.projects.trader.enums.ErrorCode;
import com.sb.projects.trader.enums.StrategyOrderStatus;
import com.sb.projects.trader.enums.StrategyStatus;
import com.sb.projects.trader.enums.StrategyType;
import com.sb.projects.trader.exceptions.BaseTraderException;
import com.sb.projects.trader.repository.StrategyOrderRepository;
import com.sb.projects.trader.repository.StrategyRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class StrategyServiceImpl implements StrategyService {
    private final StrategyRepository strategyRepository;
    private final StrategyOrderService strategyOrderService;

    @Override
    public List<Strategy> getStrategyForUser(String userId, StrategyType strategyType) {
        try {
            return strategyRepository.findByUserIdAndStrategyTypeAndStatus(userId, strategyType, StrategyStatus.Ready);
        } catch (Exception ex) {
            log.error("Unknown error retrieving strategy for user: '{}' and type: '{}'", userId, strategyType);
            return List.of();
        }
    }

    @Override
    public List<StrategyOrder> getPendingStrategyOrders() {
        try {
            return strategyOrderService.getStrategyOrders(StrategyOrderStatus.Ready);
        } catch (Exception ex){
            log.error("Unknown error retrieving strategy orders with status: '{}'", StrategyOrderStatus.Ready);
            return List.of();
        }
    }

    @Override
    public Optional<StrategyOrder> updateStrategyOrderStatus(long strategyOrderid,
                                          StrategyOrderStatus strategyOrderStatus) throws BaseTraderException {
        try {

            return Optional.of(strategyOrderService
                    .updataStrategyOrderStatus(strategyOrderid, strategyOrderStatus));

        } catch (BaseTraderException ex) {

            log.error("Strategy Order id: '{}' not found in DB", strategyOrderid);
            return Optional.empty();

        } catch (Exception ex){

            log.error("Unknown error updating strategy order id: '{}' to status: '{}'",
                    strategyOrderid, strategyOrderStatus);
            return Optional.empty();

        }
    }

    @Override
    @Transactional
    public Optional<Strategy> updateStrategyStatus(long strategyId, StrategyStatus previous, StrategyStatus strategyStatus) {
        try {
            var strategy = strategyRepository.findById(strategyId);

            if (strategy.isPresent()) {
                if (strategy.get().getStatus() == previous) {
                    strategy.get().setStatus(strategyStatus);
                    return Optional.ofNullable(strategyRepository.save(strategy.get()));
                }

                return Optional.empty();
            }

            log.error("Error retrieving strategy id: '{}'", strategyId);
            throw new BaseTraderException(ErrorCode.DBError,
                    String.format("Error retrieving strategy id: '%s' from DB", strategyId), null);
        } catch (Exception ex){
            log.error("Unknown error updating strategy id: '{}' with status: '{}'", strategyId, strategyStatus);
            throw new BaseTraderException(ErrorCode.DBError, "Unknown error updating strategy", ex);
        }
    }

    @Override
    public Optional<Strategy> updateAggregatedInvestment(long strategyId, double aggregatedInvestment) {
        try {
            var strategy = strategyRepository.findById(strategyId);
            if (strategy.isPresent()) {
                var bigAggregatedInvestment = BigDecimal.valueOf(strategy.get().getAggregateInvestment())
                        .add(BigDecimal.valueOf(aggregatedInvestment));

                strategy.get().setAggregateInvestment(bigAggregatedInvestment.doubleValue());
                return Optional.ofNullable(strategyRepository.save(strategy.get()));
            }

            log.error("Error updating strategy : '{}' with aggregatedInvestment: '{}'",
                    strategyId, aggregatedInvestment);
            throw new BaseTraderException(ErrorCode.DBError, String.format("Error updating strategy : '%s' with aggregatedInvestment: '%s'",
                    strategyId, aggregatedInvestment), null);
        } catch (Exception ex) {
            log.error("Unknown error updating strategy: '{}' with aggregatedInvestment: '{}'",
                    strategyId, aggregatedInvestment);
            log.error("Exception details for update: '{}'", ex);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Strategy> getStrategy(long strategyId) {
        try {
            return strategyRepository.findById(strategyId);
        } catch (Exception ex){
            log.error("Unknown error retrieveing strategy id: '{}' with error: {}", strategyId, ex);
            return Optional.empty();
        }
    }
}
