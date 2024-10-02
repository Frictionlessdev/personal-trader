package com.sb.projects.trader.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
public class MockServiceConfig {

    @Value("${mocks.staticDataService:false}")
    public boolean mockStaticDataService;

    @Value("${mocks.paytmServices:false}")
    public boolean mockPaytmServices;

    @Value("${mocks.paytmServices.port:9000}")
    public int mockPaytmServicesPort;

    @Value("${mocks.paytmServices.http400error:true}")
    public boolean mockPaytmServices400Error;
}