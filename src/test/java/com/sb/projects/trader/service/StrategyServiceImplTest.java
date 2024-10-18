package com.sb.projects.trader.service;

import com.sb.projects.trader.entity.StrategyOrder;
import com.sb.projects.trader.enums.StrategyOrderStatus;
import com.sb.projects.trader.enums.StrategyType;
import com.sb.projects.trader.repository.StrategyRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class StrategyServiceImplTest {

    StrategyService strategyService;

    @Mock
    StrategyRepository strategyRepository;

    @Mock
    StrategyOrderService strategyOrderService;

    @BeforeEach
    void setUp(){
        strategyService = spy(new StrategyServiceImpl(strategyRepository, strategyOrderService));
    }

    @Test
    void retrieveStrategySuccessfully(){
        strategyService.getStrategyForUser("test@test.com", StrategyType.ADVANCED_SIP);
        verify(strategyRepository).findByUserIdAndStrategyType("test@test.com", StrategyType.ADVANCED_SIP);
    }


    @Test
    void retrievePendingStrategyOrdersSuccessfully() {
        strategyService.getPendingStrategyOrders();
        verify(strategyOrderService).getStrategyOrders(StrategyOrderStatus.Ready);
    }

    @Test
    void updateStrategyOrderStatus() {
        Mockito.when(strategyOrderService.updataStrategyOrderStatus(1L, StrategyOrderStatus.Ready))
                .thenReturn(mock(StrategyOrder.class));
        strategyService.updateStrategyOrderStatus(1L, StrategyOrderStatus.Ready);
        verify(strategyOrderService).updataStrategyOrderStatus(1L, StrategyOrderStatus.Ready);
    }
}