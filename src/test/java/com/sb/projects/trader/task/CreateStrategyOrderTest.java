package com.sb.projects.trader.task;

import com.sb.projects.trader.entity.Strategy;
import com.sb.projects.trader.entity.StrategyOrder;
import com.sb.projects.trader.enums.StrategyStatus;
import com.sb.projects.trader.enums.StrategyType;
import com.sb.projects.trader.service.StrategyOrderService;
import com.sb.projects.trader.service.StrategyService;
import com.sb.projects.trader.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CreateStrategyOrderTest {

    CreateStrategyOrder createStrategyOrder;

    @Mock
    private StrategyService strategyService;

    @Mock
    private StrategyOrderService strategyOrderService;

    @Mock
    private UserService userService;

    @Captor
    ArgumentCaptor<StrategyOrder> actual;

    @BeforeEach
    void setUp(){
        createStrategyOrder = spy(new CreateStrategyOrder(strategyService, strategyOrderService, userService));
    }

    @Test
    void validateCalculationOfEachStrategyOrderItem(){
        Strategy strategyItem1 = new Strategy();
        strategyItem1.setStrategyType(StrategyType.ADVANCED_SIP);
        strategyItem1.setAllocationPercentage(20D);
        strategyItem1.setTotalMonthlyInvestment(70000D);
        strategyItem1.setUserId("test@test.com");
        strategyItem1.setSecurityId("8506");
        strategyItem1.setTradingDaysInMonth(20);

        Strategy strategyItem2 = new Strategy();
        strategyItem2.setStrategyType(StrategyType.ADVANCED_SIP);
        strategyItem2.setAllocationPercentage(30D);
        strategyItem2.setTotalMonthlyInvestment(70000D);
        strategyItem2.setUserId("test@test.com");
        strategyItem2.setSecurityId("8507");
        strategyItem2.setTradingDaysInMonth(20);

        when(strategyService.getStrategyForUser(Mockito.any(), Mockito.any())).thenReturn(Arrays.asList(
                strategyItem1,
                strategyItem2
        ));

        createStrategyOrder.calculateStrategyOrder();

        verify(createStrategyOrder).calculateStrategyOrderItem(strategyItem1);
        verify(createStrategyOrder).calculateStrategyOrderItem(strategyItem2);
    }

    @Test
    void validateCalculationOfNoPreviousStrategyOrder(){
        Strategy strategyItem = new Strategy();
        strategyItem.setStrategyType(StrategyType.ADVANCED_SIP);
        strategyItem.setAllocationPercentage(20D);
        strategyItem.setTotalMonthlyInvestment(70000D);
        strategyItem.setUserId("test@test.com");
        strategyItem.setSecurityId("8506");
        strategyItem.setTradingDaysInMonth(20);

        StrategyOrder expected = new StrategyOrder();
        expected.setStrategyType(StrategyType.ADVANCED_SIP);
        expected.setUserId("test@test.com");
        expected.setSecurityId("8506");
        expected.setInvestment(700D);
        expected.setCurrentMktPrice(266.92D);
        expected.setPercentChange(0.0D);
        expected.setCurrentMktPrice(0.0D);
        expected.setOrderPrice(0.0D);

        when(strategyOrderService.getStrategyOrder(strategyItem.getSecurityId(), strategyItem.getUserId()))
                .thenReturn(Optional.empty());

        createStrategyOrder.calculateStrategyOrderItem(strategyItem);

        verify(strategyOrderService).save(actual.capture());
        assertThat(actual.getValue(), Matchers.samePropertyValuesAs(expected));
    }

    @Test
    void validateCalculationOfStrategyOrder(){
        Strategy strategyItem = new Strategy();
        strategyItem.setId(1L);
        strategyItem.setStrategyType(StrategyType.ADVANCED_SIP);
        strategyItem.setAllocationPercentage(20D);
        strategyItem.setTotalMonthlyInvestment(70000D);
        strategyItem.setAggregateInvestment(0D);
        strategyItem.setUserId("test@test.com");
        strategyItem.setSecurityId("8506");
        strategyItem.setTradingDaysInMonth(10);
        strategyItem.setStatus(StrategyStatus.Ready);

        Strategy strategyItemUpdatedStatus = new Strategy();
        strategyItemUpdatedStatus.setId(1L);
        strategyItemUpdatedStatus.setStrategyType(StrategyType.ADVANCED_SIP);
        strategyItemUpdatedStatus.setAllocationPercentage(20D);
        strategyItemUpdatedStatus.setTotalMonthlyInvestment(70000D);
        strategyItemUpdatedStatus.setAggregateInvestment(0D);
        strategyItemUpdatedStatus.setUserId("test@test.com");
        strategyItemUpdatedStatus.setSecurityId("8506");
        strategyItemUpdatedStatus.setTradingDaysInMonth(10);
        strategyItemUpdatedStatus.setStatus(StrategyStatus.Processing);

        Strategy strategyItemUpdatedStatus1 = new Strategy();
        strategyItemUpdatedStatus1.setId(1L);
        strategyItemUpdatedStatus1.setStrategyType(StrategyType.ADVANCED_SIP);
        strategyItemUpdatedStatus1.setAllocationPercentage(20D);
        strategyItemUpdatedStatus1.setTotalMonthlyInvestment(70000D);
        strategyItemUpdatedStatus1.setAggregateInvestment(0D);
        strategyItemUpdatedStatus1.setUserId("test@test.com");
        strategyItemUpdatedStatus1.setSecurityId("8506");
        strategyItemUpdatedStatus1.setTradingDaysInMonth(10);
        strategyItemUpdatedStatus1.setStatus(StrategyStatus.Processed);

        StrategyOrder previous = new StrategyOrder();
        previous.setId(1L);
        previous.setStrategyType(StrategyType.ADVANCED_SIP);
        previous.setUserId("test@test.com");
        previous.setSecurityId("8506");
        previous.setInvestment(7000D);
        previous.setCurrentMktPrice(266.92D);
        previous.setPercentChange(0.0D);
        previous.setCurrentMktPrice(0.0D);
        previous.setOrderPrice(0.0D);

        when(strategyOrderService.getStrategyOrder(strategyItem.getSecurityId(), strategyItem.getUserId()))
                .thenReturn(Optional.of(previous));

        when(strategyService.updateStrategyStatus(1L, StrategyStatus.Ready, StrategyStatus.Processing))
                .thenReturn(Optional.of(strategyItemUpdatedStatus));

        when(strategyService.updateStrategyStatus(1L, StrategyStatus.Processing, StrategyStatus.Processed))
                .thenReturn(Optional.of(strategyItemUpdatedStatus1));

        Optional<StrategyOrder> actual = createStrategyOrder.calculateStrategyOrderItem(strategyItem);

        verify(strategyService).updateAggregatedInvestment(strategyItem.getId(), strategyItem.getAggregateInvestment() +
                (strategyItem.getTotalMonthlyInvestment()*(strategyItem.getAllocationPercentage()/100)) / strategyItem.getTradingDaysInMonth());
    }
}