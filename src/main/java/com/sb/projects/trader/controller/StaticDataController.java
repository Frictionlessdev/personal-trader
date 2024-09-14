package com.sb.projects.trader.controller;

import com.sb.projects.trader.DTO.SecurityDTO;
import com.sb.projects.trader.enums.InstrumentType;
import com.sb.projects.trader.exceptions.BaseTraderException;
import com.sb.projects.trader.service.StaticDataService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("static-data")
@AllArgsConstructor
public class StaticDataController {

    private StaticDataService staticDataService;

    @GetMapping("security/master/{securityType}")
    public List<SecurityDTO> getSecurityMaster(@PathVariable("securityType") InstrumentType instrumentType) throws BaseTraderException {
        return staticDataService.getSecuritiesMaster(instrumentType);
    }
}
