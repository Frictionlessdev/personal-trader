package com.sb.projects.trader.task;

import com.sb.projects.trader.entity.Strategy;
import com.sb.projects.trader.entity.StrategyOrder;
import com.sb.projects.trader.enums.StrategyType;
import com.sb.projects.trader.service.StrategyService;
import com.sb.projects.trader.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CreateStrategyOrderTest {

    CreateStrategyOrder createStrategyOrder;

    @Mock
    private StrategyService strategyService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp(){
        createStrategyOrder = spy(new CreateStrategyOrder(strategyService, userService));
    }

    @Test
    void validateCalculationOfEachStrategyOrderItem(){
        Strategy strategyItem1 = new Strategy();
        strategyItem1.setStrategyType(StrategyType.ADVANCED_SIP);
        strategyItem1.setAllocationPercentage(20D);
        strategyItem1.setTotalMonthlyInvestment(70000D);
        strategyItem1.setUser("test@test.com");
        strategyItem1.setSecurityId("8506");

        Strategy strategyItem2 = new Strategy();
        strategyItem1.setStrategyType(StrategyType.ADVANCED_SIP);
        strategyItem1.setAllocationPercentage(30D);
        strategyItem1.setTotalMonthlyInvestment(70000D);
        strategyItem1.setUser("test@test.com");
        strategyItem1.setSecurityId("8507");

        when(strategyService.getStrategyForUser(Mockito.any(), Mockito.any())).thenReturn(Arrays.asList(
                strategyItem1,
                strategyItem2
        ));

        createStrategyOrder.calculateStrategyOrder();

        verify(createStrategyOrder).calculateStrategyOrderItem(strategyItem1);
        verify(createStrategyOrder).calculateStrategyOrderItem(strategyItem2);
    }

    @Test
    void validateCalculationOfStrategyOrder(){
        Strategy strategyItem = new Strategy();
        strategyItem.setStrategyType(StrategyType.ADVANCED_SIP);
        strategyItem.setAllocationPercentage(20D);
        strategyItem.setTotalMonthlyInvestment(70000D);
        strategyItem.setUser("test@test.com");
        strategyItem.setSecurityId("8506");

        StrategyOrder expected = new StrategyOrder();
        expected.setStrategyType(StrategyType.ADVANCED_SIP);
        expected.setUser("test@test.com");
        expected.setSecurityId("8506");
        expected.setAggregateInvestment(700D);
        expected.setCurrentMktPrice(266.92D);
        expected.setPercentChange(-1D);

        StrategyOrder actual = createStrategyOrder.calculateStrategyOrderItem(strategyItem);

        assertThat(actual, Matchers.samePropertyValuesAs(expected));
    }
}