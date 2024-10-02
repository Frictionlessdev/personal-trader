package com.sb.projects.trader.config;

import com.sb.projects.trader.DTO.BrokerTokenDTO;
import com.sb.projects.trader.DTO.paytm.*;
import com.sb.projects.trader.entity.Order;
import com.sb.projects.trader.exceptions.BaseTraderException;
import com.sb.projects.trader.repository.OrderRepository;
import com.sb.projects.trader.repository.TokenRepository;
import com.sb.projects.trader.service.*;
import com.sb.projects.trader.service.paytm.PaytmOrderProcessingService;
import com.sb.projects.trader.service.paytm.PaytmTokenService;
import com.sb.projects.trader.service.paytm.PaytmTradeService;
import com.sb.projects.trader.task.SubmitTrade;
import com.sb.projects.trader.transformer.BaseEntityTransformer;
import com.sb.projects.trader.transformer.OrderTransformer;
import com.sb.projects.trader.utils.ReactiveWebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApplicationContext {

    @Autowired
    ApplicationConfig applicationConfig;

    @Bean
    public ReactiveWebClient<PaytmOrderDTO, PaytmOrderRequestDTO, PaytmErrorDTO> paytmTradeDTOReactiveWebClient(@Qualifier("paytmWebClient") WebClient paytmWebClient) {
        return new ReactiveWebClient<>(paytmWebClient, PaytmOrderDTO.class, PaytmOrderRequestDTO.class, PaytmErrorDTO.class);
    }

    @Bean
    public ReactiveWebClient<PaytmTokenDTO, PaytmTokenRequestDTO, PaytmErrorDTO> paytmTokenDTOReactiveWebClient(@Qualifier("paytmWebClient") WebClient paytmWebClient) {
        return new ReactiveWebClient<>(paytmWebClient, PaytmTokenDTO.class, PaytmTokenRequestDTO.class, PaytmErrorDTO.class);
    }

    @Bean
    public BrokerService<PaytmOrderDTO, PaytmOrderRequestDTO> paytmBrokerService(
            @Qualifier("paytmTradeDTOReactiveWebClient") ReactiveWebClient reactiveWebClient,
            BrokerTokenService paytmTokenService) {
            return new PaytmTradeService(paytmTokenService, reactiveWebClient);
    }

    @Bean
    public BrokerTokenService<BrokerTokenDTO> paytmTokenService(TokenRepository tokenRepository,
            @Qualifier("paytmTokenDTOReactiveWebClient") ReactiveWebClient reactiveWebClient) throws BaseTraderException {
        return new PaytmTokenService(applicationConfig.apiKey,
                applicationConfig.apiSecret, applicationConfig.requestToken,
                reactiveWebClient, tokenRepository);
    }

    @Bean
    public OrderTransformer orderTransformer(){
        return new OrderTransformer();
    }

    @Bean
    public OrderService orderService(OrderRepository orderRepository, OrderTransformer orderTransformer){
        return new OrderServiceImpl(orderRepository, orderTransformer);
    }

    @Bean
    public BaseEntityTransformer<Order, PaytmOrderRequestDTO> paytmOrderRequestDTOTransformer(){
        return new BaseEntityTransformer<Order, PaytmOrderRequestDTO>() {};
    }

    @Bean
    public Runnable submitTradeTask(OrderService orderService,
                                    @Qualifier("paytmBrokerService") BrokerService<PaytmOrderDTO, PaytmOrderRequestDTO> paytmOrderService,
                                    @Qualifier("paytmOrderRequestDTOTransformer") BaseEntityTransformer<Order, PaytmOrderRequestDTO> paytmOrderRequestDTOBaseEntityTransformerTransformer){
        return new SubmitTrade(orderService, paytmOrderService, paytmOrderRequestDTOBaseEntityTransformerTransformer);
    }

    @Bean
    public OrderProcessingService orderProcessingService(Runnable submitTradeTask){
        return new PaytmOrderProcessingService(applicationConfig.processorInterval,
                applicationConfig.processorInitialDelay, submitTradeTask);
    }
}
