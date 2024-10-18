package com.sb.projects.trader.task;

import com.sb.projects.trader.DTO.BrokerErrorDTO;
import com.sb.projects.trader.DTO.DataTransferObject;
import com.sb.projects.trader.DTO.OrderDTO;
import com.sb.projects.trader.DTO.paytm.PaytmLivePriceDTO;
import com.sb.projects.trader.DTO.paytm.PaytmOrderDTO;
import com.sb.projects.trader.DTO.paytm.PaytmOrderRequestDTO;
import com.sb.projects.trader.entity.Order;
import com.sb.projects.trader.entity.StrategyOrder;
import com.sb.projects.trader.enums.*;
import com.sb.projects.trader.exceptions.BrokerHttpException;
import com.sb.projects.trader.service.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CreateOrderTest {

    CreateOrder createOrder;

    @Mock
    OrderService orderService;

    @Mock
    StrategyService strategyService;

    @Mock
    BrokerPriceService<PaytmLivePriceDTO> brokerPriceService;

    @Captor
    ArgumentCaptor<StrategyOrder> actualStrategyOrder;

    @BeforeEach
    void setUp(){
        createOrder = spy(new CreateOrder(orderService, strategyService, brokerPriceService));
    }

    @Test
    void createOrderFromStrategyOrders(){
        StrategyOrder strategyOrderItem = new StrategyOrder();
        strategyOrderItem.setOrderPrice(0.0);
        strategyOrderItem.setUserId("test@test.com");
        strategyOrderItem.setSecurityId("8506");
        strategyOrderItem.setStrategyType(StrategyType.ADVANCED_SIP);
        strategyOrderItem.setCurrentMktPrice(0.0D);
        strategyOrderItem.setInvestment(1400D);
        strategyOrderItem.setPercentChange(0.0D);
        strategyOrderItem.setOrderPrice(0.0D);

        StrategyOrder strategyOrderItem2 = new StrategyOrder();
        strategyOrderItem2.setOrderPrice(0.0);
        strategyOrderItem2.setUserId("test@test.com");
        strategyOrderItem2.setSecurityId("8506");
        strategyOrderItem2.setStrategyType(StrategyType.ADVANCED_SIP);
        strategyOrderItem2.setCurrentMktPrice(0.0D);
        strategyOrderItem2.setInvestment(1400D);
        strategyOrderItem2.setPercentChange(0.0D);
        strategyOrderItem2.setOrderPrice(0.0D);

        when(strategyService.getPendingStrategyOrders())
                .thenReturn(Arrays.asList(strategyOrderItem, strategyOrderItem2));

        createOrder.processPendingStrategyOrders();

        verify(createOrder, times(2)).processPendingStrategyOrder(actualStrategyOrder.capture());
        assertThat(actualStrategyOrder.getAllValues().get(0), Matchers.samePropertyValuesAs(strategyOrderItem));
        assertThat(actualStrategyOrder.getAllValues().get(1), Matchers.samePropertyValuesAs(strategyOrderItem2));
    }

    @Test
    void createOrderFromStrategyOrder() {
        StrategyOrder strategyOrderItem = new StrategyOrder();
        strategyOrderItem.setId(1L);
        strategyOrderItem.setOrderPrice(0.0);
        strategyOrderItem.setUserId("test@test.com");
        strategyOrderItem.setSecurityId("8506");
        strategyOrderItem.setStrategyType(StrategyType.ADVANCED_SIP);
        strategyOrderItem.setCurrentMktPrice(0.0D);
        strategyOrderItem.setInvestment(1400D);
        strategyOrderItem.setPercentChange(0.0D);
        strategyOrderItem.setOrderPrice(0.0D);
        strategyOrderItem.setExchange(Exchange.NSE);
        strategyOrderItem.setInstrumentType(InstrumentType.ETF);

        Mono<PaytmLivePriceDTO> monoPaytmLivePriceDTO = Mono.just(PaytmLivePriceDTO.builder()
                .lastTradeTime(1412503198)
                .found(true)
                .changeAbsolute(-1.4000134D)
                .changePercent(-0.52D)
                .lastPrice(265.52D)
                .securityId(8506)
                .build());

        when(brokerPriceService.getLivePrice("8506", InstrumentType.ETF, Exchange.NSE))
                .thenReturn(monoPaytmLivePriceDTO);

        createOrder.processPendingStrategyOrder(strategyOrderItem);

        OrderDTO orderDTO = OrderDTO.builder()
                .price(265.52D)
                .userId(strategyOrderItem.getUserId())
                .quantity((int) Math.floor(strategyOrderItem.getInvestment() / 265.52D))
                .exchange(strategyOrderItem.getExchange())
                .status(OrderStatus.Saved)
                .securityId(strategyOrderItem.getSecurityId()).build();

        verify(orderService).save(orderDTO);
        verify(strategyService).updateStrategyOrderStatus(1L, StrategyOrderStatus.Processed);
    }

    @Test
    public void errorCreatingOrderFromStrategyOrder(){
        BrokerErrorDTO brokerErrorDTO = BrokerErrorDTO.builder()
                .error_code("test error code").status("error").message("error message").build();

        when(brokerPriceService.getLivePrice(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Mono.error(new BrokerHttpException(HttpStatus.BAD_REQUEST,
                        ErrorCode.RemoteIOError, "Error from remote broker", null, brokerErrorDTO)));

        createOrder.processPendingStrategyOrder(Mockito.mock(StrategyOrder.class));

        verifyNoInteractions(orderService);
        verify(strategyService).updateStrategyOrderStatus(Mockito.any(), eq(StrategyOrderStatus.Rejected));
    }

}