package com.sb.projects.trader.repository;

import com.sb.projects.trader.entity.Security;
import com.sb.projects.trader.enums.InstrumentType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SecurityRepository extends CrudRepository<Security, Long> {
    List<Security> findByName(String name);
    Security findBySecurityId(String securityId);

    List<Security> findByInstrumentType(InstrumentType instrumentType);
}