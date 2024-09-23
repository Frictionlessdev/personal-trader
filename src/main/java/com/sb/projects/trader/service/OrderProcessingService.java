package com.sb.projects.trader.service;

import com.sb.projects.trader.DTO.DataTransferObject;
import com.sb.projects.trader.DTO.OrderDTO;
import com.sb.projects.trader.repository.OrderRepository;
import lombok.AllArgsConstructor;

import java.util.concurrent.ExecutorService;

public interface OrderProcessingService {
    void processOrder();
}
