package com.sb.projects.trader.service;

import com.sb.projects.trader.DTO.SecurityDTO;
import com.sb.projects.trader.entity.Security;
import com.sb.projects.trader.enums.InstrumentType;
import com.sb.projects.trader.exceptions.BaseTraderException;

import java.util.List;

public interface StaticDataService {
    List<SecurityDTO> getSecuritiesMaster(InstrumentType instrumentType) throws BaseTraderException;
    List<Security> saveAll(List<SecurityDTO> securities) throws BaseTraderException;
}
