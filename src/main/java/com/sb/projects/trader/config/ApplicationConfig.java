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

    @Value("${trader.broker.paytm.auth.requestToken}")
    public String requestToken;

    @Value("${trader.broker.paytm.baseUrl}")
    public String paytmBaseUrl;

    @Value("${trader.order.processor.initialDelay:100}")
    public long processorInitialDelay;

    @Value("${trader.order.processor.interval:500}")
    public long processorInterval;

    @Value("${trader.order.processor.threadPool.size:1}")
    public int processorThreadPoolSize;
}
