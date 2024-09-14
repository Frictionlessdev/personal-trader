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
}