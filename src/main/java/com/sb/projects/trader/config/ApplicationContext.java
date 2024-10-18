package com.sb.projects.trader.config;

import com.sb.projects.trader.DTO.BrokerErrorDTO;
import com.sb.projects.trader.DTO.BrokerTokenDTO;
import com.sb.projects.trader.DTO.DataTransferObject;
import com.sb.projects.trader.DTO.paytm.*;
import com.sb.projects.trader.entity.Order;
import com.sb.projects.trader.exceptions.BaseTraderException;
import com.sb.projects.trader.repository.OrderRepository;
import com.sb.projects.trader.repository.StrategyOrderRepository;
import com.sb.projects.trader.repository.StrategyRepository;
import com.sb.projects.trader.repository.TokenRepository;
import com.sb.projects.trader.service.*;
import com.sb.projects.trader.service.paytm.PaytmOrderProcessingService;
import com.sb.projects.trader.service.paytm.PaytmPriceService;
import com.sb.projects.trader.service.paytm.PaytmTokenService;
import com.sb.projects.trader.service.paytm.PaytmTradeService;
import com.sb.projects.trader.task.CreateOrder;
import com.sb.projects.trader.task.CreateStrategyOrder;
import com.sb.projects.trader.task.SubmitOrder;
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
    public ReactiveWebClient<BrokerErrorDTO, PaytmOrderRequestDTO, PaytmErrorDTO> paytmTradeDTOReactiveWebClient(@Qualifier("paytmWebClient") WebClient paytmWebClient) {
        return new ReactiveWebClient<>(paytmWebClient, BrokerErrorDTO.class, PaytmOrderRequestDTO.class, PaytmErrorDTO.class);
    }

    @Bean
    public ReactiveWebClient<PaytmTokenDTO, PaytmTokenRequestDTO, PaytmErrorDTO> paytmTokenDTOReactiveWebClient(@Qualifier("paytmWebClient") WebClient paytmWebClient) {
        return new ReactiveWebClient<>(paytmWebClient, PaytmTokenDTO.class, PaytmTokenRequestDTO.class, PaytmErrorDTO.class);
    }

    @Bean
    public ReactiveWebClient<PaytmLivePriceDTO, DataTransferObject, BrokerErrorDTO> paytmPriceReactiveWebClient(@Qualifier("paytmWebClient") WebClient paytmWebClient) {
        return new ReactiveWebClient<>(paytmWebClient, PaytmLivePriceDTO.class, DataTransferObject.class, BrokerErrorDTO.class);
    }

    @Bean
    public BrokerService<BrokerErrorDTO, PaytmOrderRequestDTO> paytmBrokerService(
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
    public BrokerPriceService<PaytmLivePriceDTO> paytmPriceDTOBrokerService(BrokerTokenService paytmTokenService,
                                                                            @Qualifier("paytmPriceReactiveWebClient") ReactiveWebClient reactiveWebClient){
        return new PaytmPriceService(paytmTokenService, reactiveWebClient);
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
    public Runnable submitOrderTask(OrderService orderService,
                                    @Qualifier("paytmBrokerService") BrokerService<BrokerErrorDTO, PaytmOrderRequestDTO> paytmOrderService,
                                    @Qualifier("paytmOrderRequestDTOTransformer") BaseEntityTransformer<Order, PaytmOrderRequestDTO> paytmOrderRequestDTOBaseEntityTransformerTransformer){
        return new SubmitOrder(applicationConfig.orderProcessorExecution, orderService, paytmOrderService, paytmOrderRequestDTOBaseEntityTransformerTransformer);
    }

    @Bean
    public OrderProcessingService orderProcessingService(Runnable submitOrderTask){
        return new PaytmOrderProcessingService(applicationConfig.processorInterval,
                applicationConfig.processorInitialDelay, submitOrderTask);
    }

    @Bean
    public StrategyService strategyService(StrategyRepository strategyRepository,
                                           StrategyOrderService strategyOrderService){
        return new StrategyServiceImpl(strategyRepository, strategyOrderService);
    }

    @Bean
    public UserService userService(){
        return new UserServiceImpl();
    }

    @Bean
    public StrategyOrderService strategyOrderService(StrategyOrderRepository strategyOrderRepository){
        return new StrategyOrderServiceImpl(strategyOrderRepository);
    }

    @Bean
    public CreateStrategyOrder createStrategyOrderTask(StrategyService strategyService, StrategyOrderService strategyOrderService,
                                                       UserService userService){
        return new CreateStrategyOrder(strategyService, strategyOrderService, userService);
    }

    @Bean
    public CreateOrder createOrderTask(OrderService orderService, StrategyService strategyService,
                                       BrokerPriceService<PaytmLivePriceDTO> paytmPriceBrokerService){
        return new CreateOrder(orderService, strategyService, paytmPriceBrokerService);
    }

    @Bean
    public StrategyProcessingService strategyProcessingService(Runnable createStrategyOrderTask,
                                                               Runnable createOrderTask){
        return new StrategyProcessingService(applicationConfig.processorInitialDelay,
                applicationConfig.processorInterval, createStrategyOrderTask, createOrderTask);
    }
}
