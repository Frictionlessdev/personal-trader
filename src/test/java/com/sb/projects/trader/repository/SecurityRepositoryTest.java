package com.sb.projects.trader.repository;

import com.sb.projects.trader.entity.Security;
import com.sb.projects.trader.enums.Exchange;
import com.sb.projects.trader.enums.InstrumentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:dcbapp",
        "spring.jps.hibernate.ddl-auto=create-drop"
}, showSql = true)
public class SecurityRepositoryTest {

    @Autowired
    SecurityRepository securityRepository;

    private Security security;

    @BeforeEach
    public void setUp(){
        security = new Security();
        security.setSecurityId("test-security-id");
        security.setExchange("nse");
        security.setSeries("test-series");
        security.setSegment("test-segment");
        security.setName("test-name");
        security.setInstrumentType(InstrumentType.ETF);
        security.setFreezeQuantity("test-freeze-quantity");
        security.setLowerLimit("test-lower-limit");
        security.setSymbol("test-symbol");
        security.setStrikePrice("test-strike-price");
        security.setExpiryDate("test-date");
        security.setUpperLimit("test-upper-limit");
        security.setLotSize("test-lot-size");
        security.setTickSize("test-tick-size");

        securityRepository.save(security);
    }

    @Test
    void givenSecurity_whenSaved_thenCanBeFoundById(){
        security = securityRepository.findBySecurityId("test-security-id");
    }

    @AfterEach
    public void tearDown(){
        securityRepository.delete(security);
    }
}