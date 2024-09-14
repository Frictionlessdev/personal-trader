package com.sb.projects.trader.DTO.mock;

import com.sb.projects.trader.DTO.SecurityDTO;
import com.sb.projects.trader.enums.InstrumentType;

import java.util.ArrayList;
import java.util.List;

public class MockStaticDataGenerator {
    public List<SecurityDTO> generateETFSecurityMaster() {

        List<SecurityDTO> securities = new ArrayList<>();
        securities.add(SecurityDTO.builder()
                .id("19084")
                .symbol("ITBEES")
                .name("Nippon India Nifty IT ETF")
                .series("EQ")
                .tickSize("1.0000")
                .lotSize("1")
                .instrumentType(InstrumentType.ETF)
                .segment("E")
                .exchange("NSE")
                .upperLimit("50.9800")
                .lowerLimit("33.9900")
                .expiryDate("")
                .strikePrice("")
                .freezeQuantity("2352953").build());

        securities.add(SecurityDTO.builder()
                .id("11319")
                .symbol("LICNETFN50")
                .name("LIC Nifty ETF")
                .series("EQ")
                .tickSize("1.0000")
                .lotSize("1")
                .instrumentType(InstrumentType.ETF)
                .segment("E")
                .exchange("NSE")
                .upperLimit("318.2400")
                .lowerLimit("212.1600")
                .expiryDate("")
                .strikePrice("")
                .freezeQuantity("377069").build());

        return securities;

    }
}
