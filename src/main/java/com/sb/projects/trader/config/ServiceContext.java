package com.sb.projects.trader.config;

import com.sb.projects.trader.DTO.BrokerTokenDTO;
import com.sb.projects.trader.DTO.paytm.PaytmOrderDTO;
import com.sb.projects.trader.DTO.paytm.PaytmOrderRequestDTO;
import com.sb.projects.trader.DTO.paytm.PaytmTokenDTO;
import com.sb.projects.trader.DTO.paytm.PaytmTokenRequestDTO;
import com.sb.projects.trader.exceptions.BaseTraderException;
import com.sb.projects.trader.repository.SecurityRepository;
import com.sb.projects.trader.service.BrokerService;
import com.sb.projects.trader.service.BrokerTokenService;
import com.sb.projects.trader.service.StaticDataService;
import com.sb.projects.trader.service.StaticDataServiceImpl;
import com.sb.projects.trader.service.mock.MockPaytmTradeService;
import com.sb.projects.trader.service.remote.PaytmTokenService;
import com.sb.projects.trader.service.remote.PaytmTradeService;
import com.sb.projects.trader.transformer.SecurityTransformer;
import com.sb.projects.trader.utils.ReactiveWebClient;
import io.netty.handler.logging.LogLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Configuration
@Profile("!mock")
public class ServiceContext {

    @Autowired
    ApplicationConfig applicationConfig;

    @Bean
    WebClient paytmWebClient(){
        var httpClient = HttpClient
                .create()
                .wiretap("reactor.netty.http.client.HttpClient",
                        LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);

        return WebClient.builder()
                .baseUrl(applicationConfig.paytmBaseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean
    ReactiveWebClient<PaytmTokenDTO, PaytmTokenRequestDTO> paytmTokenDTOReactiveWebClient(@Qualifier("paytmWebClient") WebClient paytmWebClient){
        return new ReactiveWebClient<>(paytmWebClient, PaytmTokenDTO.class, PaytmTokenRequestDTO.class);
    }

    @Bean
    public SecurityTransformer securityTransformer(){
        return new SecurityTransformer();
    }

    @Bean
    public StaticDataService staticDataService(SecurityRepository securityRepository, SecurityTransformer securityTransformer) {
        return new StaticDataServiceImpl(securityRepository, securityTransformer);
    }

    @Bean
    public BrokerTokenService<BrokerTokenDTO, PaytmTokenRequestDTO> paytmTokenService(
            @Qualifier("paytmTokenDTOReactiveWebClient") ReactiveWebClient reactiveWebClient) throws BaseTraderException {
        return new PaytmTokenService(reactiveWebClient);
    }

    @Bean
    public BrokerService<PaytmOrderDTO, PaytmOrderRequestDTO> brokerService(
            @Qualifier("paytmTradeDTOReactiveWebClient") ReactiveWebClient reactiveWebClient
    ){
        return new PaytmTradeService(reactiveWebClient);
    }
}
