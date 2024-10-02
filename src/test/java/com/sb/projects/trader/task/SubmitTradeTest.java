package com.sb.projects.trader.task;

import com.sb.projects.trader.DTO.BrokerErrorDTO;
import com.sb.projects.trader.DTO.OrderDTO;
import com.sb.projects.trader.DTO.paytm.PaytmOrderDTO;
import com.sb.projects.trader.DTO.paytm.PaytmOrderRequestDTO;
import com.sb.projects.trader.entity.Order;
import com.sb.projects.trader.enums.ErrorCode;
import com.sb.projects.trader.enums.Exchange;
import com.sb.projects.trader.enums.OrderStatus;
import com.sb.projects.trader.exceptions.BaseTraderException;
import com.sb.projects.trader.exceptions.BrokerHttpException;
import com.sb.projects.trader.service.BrokerService;
import com.sb.projects.trader.service.OrderService;
import com.sb.projects.trader.transformer.BaseEntityTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class SubmitTradeTest {

    @Mock
    OrderService orderService;

    @Mock
    BrokerService<PaytmOrderDTO, PaytmOrderRequestDTO> brokerService;

    BaseEntityTransformer<Order, PaytmOrderRequestDTO> baseEntityTransformer = new BaseEntityTransformer<Order, PaytmOrderRequestDTO>() {};;

    SubmitTrade submitTrade;

    @BeforeEach
    public void setUp(){
        submitTrade = new SubmitTrade(orderService, brokerService, baseEntityTransformer);
    }

    @Test
    public void tradesWithPendingStatusAreSubmitted() throws BaseTraderException {
        PaytmOrderRequestDTO paytmOrderRequestDTO = generatePaytmOrderRequest();
        OrderDTO orderDTO = generateOrderDTO();
        Mono<PaytmOrderDTO> paytmOrderDTOMono = Mono.just(generateSuccessPaytmOrderDTO());

        Mockito.doReturn(Arrays.asList(generatePendingOrder())).when(orderService).getPendingOrders();
        Mockito.doReturn(paytmOrderDTOMono).when(brokerService).submitOrder(paytmOrderRequestDTO);
        Mockito.when(orderService.submit(paytmOrderRequestDTO.getOrderId(), OrderStatus.Submitted, null)).thenReturn(orderDTO);

        submitTrade.submit(paytmOrderRequestDTO);

        verify(brokerService).submitOrder(paytmOrderRequestDTO);
        verify(orderService).submit(paytmOrderRequestDTO.getOrderId(), OrderStatus.Submitted, null);
    }

    @Test
    public void unknownErrorOnSubmittingToBroker() throws BaseTraderException {
        PaytmOrderRequestDTO paytmOrderRequestDTO = generatePaytmOrderRequest();
        Mockito.when(brokerService.submitOrder(paytmOrderRequestDTO)).thenThrow(new BaseTraderException(ErrorCode.RemoteIOError,
                "Error submitting to remote broker", null));

        OrderDTO orderDTO = generateOrderDTO();
        Mockito.when(orderService.submit(paytmOrderRequestDTO.getOrderId(), OrderStatus.Rejected, null)).thenReturn(orderDTO);

        submitTrade.submit(paytmOrderRequestDTO);

        verify(brokerService).submitOrder(paytmOrderRequestDTO);
        verify(orderService).submit(paytmOrderRequestDTO.getOrderId(), OrderStatus.Rejected, null);
    }

    @Test()
    public void httpBadRequestOnSubmittingToBroker() throws BaseTraderException {
        PaytmOrderRequestDTO paytmOrderRequestDTO = generatePaytmOrderRequest();
        BrokerErrorDTO paytmErrorDTO = new BrokerErrorDTO("error",
                "Oops! Something went wrong.", "PM_OPEN_API_400");

        Mockito.when(brokerService.submitOrder(paytmOrderRequestDTO)).thenReturn(
                Mono.error(new BrokerHttpException(HttpStatus.BAD_REQUEST, ErrorCode.RemoteBadRequest,
                        "Error http Bad Request (400) from remote broker", null, paytmErrorDTO)));

        OrderDTO orderDTO = generateOrderDTO();
        Mockito.when(orderService.submit(paytmOrderRequestDTO.getOrderId(), OrderStatus.Rejected, paytmErrorDTO)).thenReturn(orderDTO);

        submitTrade.submit(paytmOrderRequestDTO);

        verify(brokerService).submitOrder(paytmOrderRequestDTO);
        verify(orderService).submit(paytmOrderRequestDTO.getOrderId(), OrderStatus.Rejected, paytmErrorDTO);
    }

    private OrderDTO generateOrderDTO(){
        return OrderDTO.builder().userId("test@test.com")
                .id("test-order-id")
                .price(1234.9)
                .securityId("11223344")
                .quantity(199)
                .exchange(Exchange.NSE).build();
    }

    private PaytmOrderRequestDTO generatePaytmOrderRequest(){
        return PaytmOrderRequestDTO.builder()
                .orderId("test-order-id")
                .price(1234.9)
                .securityId("11223344")
                .orderType("MKT")
                .quantity(199)
                .exchange(Exchange.NSE)
                .segment("E")
                .source("N")
                .transactionType("B")
                .product("C")
                .validity("DAY")
                .offMktFlag("true").build();
    }

    private PaytmOrderDTO generateSuccessPaytmOrderDTO(){
        return PaytmOrderDTO.builder().id("test order id").status("test-status").build();
    }

    private Order generatePendingOrder(){
        Order order = new Order();
        order.setSecurityId("11223344");
        order.setPrice(1234.9);
        order.setUserId("test@test.com");
        order.setQuantity(199);
        order.setExchange(Exchange.NSE);
        order.setStatus(OrderStatus.Saved);

        return order;
    }

}