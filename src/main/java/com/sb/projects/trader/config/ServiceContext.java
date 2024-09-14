package com.sb.projects.trader.config;

import com.sb.projects.trader.repository.SecurityRepository;
import com.sb.projects.trader.service.StaticDataService;
import com.sb.projects.trader.service.StaticDataServiceImpl;
import com.sb.projects.trader.transformer.SecurityTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!mock")
public class ServiceContext {

    @Bean
    public SecurityTransformer securityTransformer(){
        return new SecurityTransformer();
    }

    @Bean
    public StaticDataService staticDataService(SecurityRepository securityRepository, SecurityTransformer securityTransformer) {
        return new StaticDataServiceImpl(securityRepository, securityTransformer);
    }
}
