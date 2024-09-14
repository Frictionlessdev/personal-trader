package com.sb.projects.trader.config;

import com.sb.projects.trader.DTO.SecurityDTO;
import com.sb.projects.trader.DTO.mock.MockStaticDataGenerator;
import com.sb.projects.trader.entity.Security;
import com.sb.projects.trader.enums.ErrorCode;
import com.sb.projects.trader.enums.InstrumentType;
import com.sb.projects.trader.exceptions.BaseTraderException;
import com.sb.projects.trader.repository.SecurityRepository;
import com.sb.projects.trader.service.SecuritiesStaticDataLoaderService;
import com.sb.projects.trader.service.StaticDataLoaderService;
import com.sb.projects.trader.service.StaticDataService;
import com.sb.projects.trader.service.StaticDataServiceImpl;
import com.sb.projects.trader.service.mock.MockStaticDataServiceImpl;
import com.sb.projects.trader.transformer.SecurityTransformer;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Profile("mock")
@Slf4j
public class MockServiceContext {

    @Autowired
    MockServiceConfig mockServiceConfig;

    @Bean
    public MockStaticDataGenerator mockStaticDataGenerator(){
        return new MockStaticDataGenerator();
    }

    @Bean
    public StaticDataService mockStaticDataService(@Qualifier("mockStaticDataGenerator") MockStaticDataGenerator mockStaticDataGenerator){
        return new MockStaticDataServiceImpl(mockStaticDataGenerator);
    }

    @Bean
    public SecurityTransformer securityTransformer(){
        return new SecurityTransformer();
    }

    @Bean
    public StaticDataService staticDataService(@Qualifier("mockStaticDataService") StaticDataService staticDataService,
                                               SecurityRepository securityRepository, SecurityTransformer securityTransformer) throws BaseTraderException {
        if (mockServiceConfig.mockStaticDataService) {
            log.info("Initializing mocks [{}]", "MockStaticDataService");
              return staticDataService;
        } else {
            return new StaticDataServiceImpl(securityRepository, securityTransformer);
        }
    }

    @Bean
    @ConditionalOnProperty(name="mocks.staticDataService", havingValue = "false")
    public StaticDataLoaderService staticDataLoaderService(@Qualifier("staticDataService") StaticDataService staticDataService){
        return new SecuritiesStaticDataLoaderService(staticDataService);
    }
}
