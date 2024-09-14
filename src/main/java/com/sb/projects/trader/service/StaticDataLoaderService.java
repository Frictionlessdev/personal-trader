package com.sb.projects.trader.service;

import com.sb.projects.trader.exceptions.BaseTraderException;
import jakarta.annotation.PostConstruct;

public interface StaticDataLoaderService {

    void Load() throws BaseTraderException;
}
