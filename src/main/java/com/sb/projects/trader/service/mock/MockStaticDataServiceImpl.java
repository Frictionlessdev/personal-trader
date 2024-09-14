package com.sb.projects.trader.service.mock;

import com.sb.projects.trader.DTO.SecurityDTO;
import com.sb.projects.trader.DTO.mock.MockStaticDataGenerator;
import com.sb.projects.trader.entity.Security;
import com.sb.projects.trader.enums.ErrorCode;
import com.sb.projects.trader.enums.InstrumentType;
import com.sb.projects.trader.exceptions.BaseTraderException;
import com.sb.projects.trader.service.StaticDataService;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class MockStaticDataServiceImpl implements StaticDataService {

    private MockStaticDataGenerator mockStaticDataGenerator;

    @Override
    public List<SecurityDTO> getSecuritiesMaster(InstrumentType instrumentType) throws BaseTraderException {
        return mockStaticDataGenerator.generateETFSecurityMaster();
    }

    @Override
    public List<Security> saveAll(List<SecurityDTO> securities) throws BaseTraderException {
        throw new BaseTraderException(ErrorCode.NotImplemented, "save all is not implemented", null);
    }
}
