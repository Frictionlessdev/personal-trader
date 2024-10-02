package com.sb.projects.trader.config;

import com.sb.projects.trader.DTO.BrokerTokenDTO;
import com.sb.projects.trader.DTO.paytm.*;
import com.sb.projects.trader.entity.Order;
import com.sb.projects.trader.exceptions.BaseTraderException;
import com.sb.projects.trader.repository.OrderRepository;
import com.sb.projects.trader.repository.SecurityRepository;
import com.sb.projects.trader.service.*;
import com.sb.projects.trader.service.paytm.PaytmOrderProcessingService;
import com.sb.projects.trader.service.paytm.PaytmTokenService;
import com.sb.projects.trader.service.paytm.PaytmTradeService;
import com.sb.projects.trader.transformer.BaseEntityTransformer;
import com.sb.projects.trader.transformer.OrderTransformer;
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
}
