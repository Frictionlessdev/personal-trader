package com.sb.projects.trader.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
public class ApplicationConfig {
    @Value("${trader.broker.paytm.auth.apiKey}")
    public String apiKey;

    @Value("${trader.broker.paytm.auth.apiSecret}")
    public String apiSecret;

    @Value("${trader.broker.paytm.baseUrl}")
    public String paytmBaseUrl;
}
