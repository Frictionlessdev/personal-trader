package com.sb.projects.trader.service;

import com.sb.projects.trader.DTO.SecurityDTO;
import com.sb.projects.trader.entity.Security;
import com.sb.projects.trader.enums.InstrumentType;
import com.sb.projects.trader.exceptions.BaseTraderException;
import com.sb.projects.trader.repository.SecurityRepository;
import com.sb.projects.trader.transformer.SecurityTransformer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
public class StaticDataServiceImpl implements StaticDataService {

    private SecurityRepository securityRepository;
    private SecurityTransformer securityTransformer;

    @Override
    public List<SecurityDTO> getSecuritiesMaster(InstrumentType instrumentType) throws BaseTraderException {
        List<Security> securities = securityRepository.findByInstrumentType(instrumentType);

        return securities.stream().map((security) -> securityTransformer.transform(security,
                (entity) -> SecurityDTO.builder()
                        .id(entity.getSecurityId())
                        .name(entity.getName())
                        .exchange(entity.getExchange())
                        .expiryDate(entity.getExpiryDate())
                        .freezeQuantity(entity.getFreezeQuantity())
                        .lotSize(entity.getLotSize())
                        .lowerLimit(entity.getLowerLimit())
                        .instrumentType(entity.getInstrumentType())
                        .segment(entity.getSegment())
                        .series(entity.getSeries())
                        .strikePrice(entity.getStrikePrice())
                        .symbol(entity.getSymbol())
                        .tickSize(entity.getTickSize())
                        .upperLimit(entity.getUpperLimit())
                        .build())).collect(Collectors.toList());
    }

    @Override
    public List<Security> saveAll(List<SecurityDTO> securities) throws BaseTraderException {
        List<Security> entities = securities.stream().map((securityDTO -> securityTransformer.transform(securityDTO, (SecurityDTO dto) -> {
            Security security = new Security();

            security.setSecurityId(dto.getId());
            security.setSymbol(dto.getSymbol());
            security.setName(dto.getName());
            security.setSeries(dto.getSeries());
            security.setTickSize(dto.getTickSize());
            security.setLotSize(dto.getLotSize());
            security.setInstrumentType(dto.getInstrumentType());
            security.setSegment(dto.getSegment());
            security.setExchange(dto.getExchange());
            security.setUpperLimit(dto.getUpperLimit());
            security.setLowerLimit(dto.getLowerLimit());
            security.setExpiryDate(dto.getExpiryDate());
            security.setStrikePrice(dto.getStrikePrice());
            security.setFreezeQuantity(dto.getFreezeQuantity());

            return security;
        }))).collect(Collectors.toList());

        return StreamSupport.stream(securityRepository.saveAll(entities).spliterator(), false)
                .collect(Collectors.toList());
    }
}
