package com.sb.projects.trader.service;

import com.sb.projects.trader.entity.StrategyOrder;
import com.sb.projects.trader.enums.ErrorCode;
import com.sb.projects.trader.enums.StrategyOrderStatus;
import com.sb.projects.trader.exceptions.BaseTraderException;
import com.sb.projects.trader.repository.StrategyOrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class StrategyOrderServiceImpl implements StrategyOrderService {
    private final StrategyOrderRepository strategyOrderRepository;

    @Override
    public Optional<StrategyOrder> getStrategyOrder(String securityId, String userId) {
        try{
            return Optional.ofNullable(strategyOrderRepository.findBySecurityIdAndUserIdAndStatus(securityId, userId,
                    StrategyOrderStatus.Ready));
        } catch (Exception ex) {
            log.error("Unknown error retrieving strategy order for security id: '{}' and user id: '{}'", securityId, userId);
            log.error("Exception details for findBySecurityIdAndUserId: '{}'", ex);
            return Optional.empty();
        }
    }

    @Override
    public Optional<StrategyOrder> save(StrategyOrder strategyOrder) {
        try {
            return Optional.ofNullable(strategyOrderRepository.save(strategyOrder));
        } catch (Exception ex) {
            log.error("Unknown error saving strategy order: '{}'", strategyOrder);
            log.error("Exception details for save: '{}'", ex);
            return Optional.empty();
        }
    }

    @Override
    public List<StrategyOrder> getStrategyOrders(StrategyOrderStatus strategyOrderStatus) {
        try {
            return strategyOrderRepository.findByStatus(strategyOrderStatus);
        } catch (Exception ex) {
            log.error("Unknown error retrieving strategyOrders for status: '{}' with error: {}", strategyOrderStatus, ex);
            return List.of();
        }
    }

    @Override
    public StrategyOrder updataStrategyOrderStatus(long strategyOrderId, StrategyOrderStatus strategyOrderStatus) {
        try {
            var strategyOrder = strategyOrderRepository.findById(strategyOrderId);

            if (strategyOrder.isPresent()){
                strategyOrder.get().setStatus(strategyOrderStatus);
                return strategyOrderRepository.save(strategyOrder.get());
            }

            log.error("Error retrieving strategy order id: '{}'", strategyOrderId);
            throw new BaseTraderException(ErrorCode.DBError, "Unknown error retrieving strategy order from DB", null);

        } catch (Exception ex){
            log.error("Unknown error updating strategy order id: '{}', strategy order status: '{}' with error",
                    strategyOrderId, strategyOrderStatus, ex);
            throw new BaseTraderException(ErrorCode.DBError, "Unknown error updating strategy order in DB", ex);
        }
    }

    @Override
    public Optional<StrategyOrder> getLastStrategyOrder(String securityId, String userId) {
        try {
            return Optional.ofNullable(strategyOrderRepository.findLatestStrategyOrder(securityId, userId));
        } catch (Exception ex) {
            log.error("Unknown error retrieving strategyOrder for security id: '{}' and user id: {} with error: {}",
                    securityId, userId, ex);
            return Optional.empty();
        }
    }
}
