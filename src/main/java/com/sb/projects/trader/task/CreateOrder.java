package com.sb.projects.trader.task;

import com.sb.projects.trader.DTO.OrderDTO;
import com.sb.projects.trader.DTO.paytm.PaytmLivePriceDTO;
import com.sb.projects.trader.entity.StrategyOrder;
import com.sb.projects.trader.enums.OrderStatus;
import com.sb.projects.trader.enums.StrategyOrderStatus;
import com.sb.projects.trader.enums.StrategyType;
import com.sb.projects.trader.service.BrokerPriceService;
import com.sb.projects.trader.service.OrderService;
import com.sb.projects.trader.service.StrategyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
@AllArgsConstructor
public class CreateOrder implements Runnable{

    private final OrderService orderService;
    private final StrategyService strategyService;
    private final BrokerPriceService<PaytmLivePriceDTO> brokerPriceService;

    @Override
    public void run() {
        try {
            log.info("Running create order task...");
            processPendingStrategyOrders();
        } catch(Exception ex) {
            log.error("Unknown error occured while processing strategy orders: {}", ex);
        }
    }

    public void processPendingStrategyOrders() {
        var strategyOrders = strategyService.getPendingStrategyOrders();

        if (strategyOrders.isEmpty()){
            log.info("Could not find and strategy orders in state: {}", StrategyOrderStatus.Ready);
            return;
        }

        strategyOrders.stream().forEach(this::processPendingStrategyOrder);
    }

    public void processPendingStrategyOrder(StrategyOrder strategyOrderItem) {
        strategyService.updateStrategyOrderStatus(strategyOrderItem.getId(), StrategyOrderStatus.Processing);
        log.info("Processing strategy order: '{}'", strategyOrderItem);

        brokerPriceService.getLivePrice(strategyOrderItem.getSecurityId(),
                strategyOrderItem.getInstrumentType(), strategyOrderItem.getExchange()).subscribe(
                d -> {
                    var dto = d.getData().getFirst();
                    if (dto.getChangePercent() < 0 && dto.getChangePercent() <= strategyOrderItem.getPercentChange()) {

                        int quantity = (int) Math.floor(strategyOrderItem.getInvestment() / dto.getLastPrice());

                        orderService.save(OrderDTO.builder()
                                        .strategyOrderId(strategyOrderItem.getId())
                                        .strategyId(strategyOrderItem.getStrategyId())
                                        .userId(strategyOrderItem.getUserId())
                                        .securityId(String.valueOf(dto.getSecurityId()))
                                        .price(strategyOrderItem.getOrderPrice())
                                        .exchange(strategyOrderItem.getExchange())
                                        .quantity(quantity)
                                        .status(OrderStatus.Saved).build());

                        log.info("Order for security '{}' with quantity '{}' and price '{}' successfully save ",
                                strategyOrderItem.getSecurityId(), dto.getLastPrice(), quantity);

                        strategyService.updateStrategyOrderStatus(strategyOrderItem.getId(), StrategyOrderStatus.Processed).ifPresentOrElse(
                                so -> {
                                    log.info("Updated strategy order status '{}' for security '{}' with quantity '{}' and price '{}' successfully save ",
                                            StrategyOrderStatus.Processed, strategyOrderItem.getSecurityId(), dto.getLastPrice(), quantity);

                                    strategyService.getStrategy(so.getStrategyId()).ifPresentOrElse(
                                            s -> {
                                                var bigMonthlyAggregatedInvestment = BigDecimal.valueOf(so.getInvestment());
                                                strategyService.updateAggregatedInvestment(s.getId(), bigMonthlyAggregatedInvestment.doubleValue());
                                            },
                                            () -> {
                                                log.error("Error retreiving strategy for updating aggregate investment: '{}'", so.getStrategyId());
                                            });
                                },
                                () -> {
                                    log.error("Error updating strategy order status '{}' for security '{}' with quantity '{}' and price '{}' ",
                                            StrategyOrderStatus.Processed, strategyOrderItem.getSecurityId(), dto.getLastPrice(), quantity);
                                }
                        );
                    } else {
                        strategyService.updateStrategyOrderStatus(strategyOrderItem.getId(), StrategyOrderStatus.Rejected).ifPresentOrElse(
                                so -> {
                                    log.info("Updated strategy order status '{}' for security '{}'",
                                            StrategyOrderStatus.Rejected, strategyOrderItem.getSecurityId());
                                },
                                () -> {
                                    log.error("Error updating strategy order status '{}' for security '{}'",
                                            StrategyOrderStatus.Rejected, strategyOrderItem.getSecurityId());
                                }
                        );

                        log.info("Not created order for security '{}' and price '{}' " +
                                        "as mkt change(%): '{}' is less than strategy order change(%) : '{}' ",
                                strategyOrderItem.getSecurityId(), dto.getLastPrice(), dto.getChangePercent(),
                                strategyOrderItem.getPercentChange());
                    }
                },
                err -> {
                    log.error("Error getting LIVE price for security '{}', details: {}",
                            strategyOrderItem.getSecurityId(), err.getMessage());

                    strategyService.updateStrategyOrderStatus(strategyOrderItem.getId(), StrategyOrderStatus.Failed);

                    log.info("Strategy order status '{}' for security '{}'successfully saved ",
                            StrategyOrderStatus.Rejected, strategyOrderItem.getSecurityId());

                });
    }
}
