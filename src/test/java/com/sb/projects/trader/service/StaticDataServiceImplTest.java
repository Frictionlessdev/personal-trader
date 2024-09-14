package com.sb.projects.trader.service;

import com.sb.projects.trader.DTO.SecurityDTO;
import com.sb.projects.trader.entity.Security;
import com.sb.projects.trader.enums.InstrumentType;
import com.sb.projects.trader.exceptions.BaseTraderException;
import com.sb.projects.trader.repository.SecurityRepository;
import com.sb.projects.trader.transformer.SecurityTransformer;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class StaticDataServiceImplTest {

    @Spy
    SecurityRepository securityRepository;

    SecurityTransformer securityTransformer = new SecurityTransformer();

    StaticDataService staticDataService;

    @Captor
    ArgumentCaptor<List<Security>> securities;

    @BeforeEach
    void setup(){
        staticDataService = new StaticDataServiceImpl(securityRepository, securityTransformer);
    }

    @Test
    void getSecuritiesMasterReturnsSecurityDTO() throws BaseTraderException {
        List<Security> securities = new ArrayList<>();
        Security security = new Security();
        security.setSecurityId("12345");
        security.setName("Test Security Name");
        securities.add(security);

        List<SecurityDTO> expected = new ArrayList<>();
        SecurityDTO securityDTO = SecurityDTO.builder()
                .id("12345")
                .name("Test Security Name").build();
        expected.add(securityDTO);

        Mockito.doReturn(securities)
                .when(securityRepository)
                .findByInstrumentType(InstrumentType.ETF);

        List<SecurityDTO> result = staticDataService.getSecuritiesMaster(InstrumentType.ETF);

        assertEquals(expected, result);
    }

    @Test
    void saveSecuritiesMasterSuccessfully() throws BaseTraderException {
        List<SecurityDTO> securityDTOs = new ArrayList<>();
        SecurityDTO securityDTO = SecurityDTO.builder()
                .id("12345")
                .name("Test Security Name")
                .upperLimit("Test upperLimit")
                .tickSize("test tickSize")
                .symbol("test Symbol")
                .strikePrice("test strickprice")
                .lowerLimit("test lowerLimit")
                .segment("test segment")
                .series("test series")
                .lotSize("test lotsize")
                .freezeQuantity("test freezeQuantity")
                .instrumentType(InstrumentType.ETF)
                .exchange("test exchange")
                .build();
        securityDTOs.add(securityDTO);

        List<Security> expected = new ArrayList<>();
        Security security = new Security();
        security.setSecurityId("12345");
        security.setName("Test Security Name");
        security.setUpperLimit("Test upperLimit");
        security.setTickSize("test tickSize");
        security.setSymbol("test Symbol");
        security.setStrikePrice("test strickprice");
        security.setLowerLimit("test lowerLimit");
        security.setSegment("test segment");
        security.setSeries("test series");
        security.setLotSize("test lotsize");
        security.setFreezeQuantity("test freezeQuantity");
        security.setInstrumentType(InstrumentType.ETF);
        security.setExchange("test exchange");
        expected.add(security);
        
        staticDataService.saveAll(securityDTOs);

        Mockito.verify(securityRepository).saveAll(securities.capture());
        List<Security> result = securities.getValue();
        assertEquals(expected.size(), result.size());
        assertThat(expected.get(0), Matchers.samePropertyValuesAs(result.get(0)));
    }
}