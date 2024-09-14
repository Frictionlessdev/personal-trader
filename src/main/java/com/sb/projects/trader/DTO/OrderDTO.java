package com.sb.projects.trader.DTO;

import com.sb.projects.trader.enums.Exchange;
import com.sb.projects.trader.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDTO implements DataTransferObject {
    private String id;
    private String securityId;
    private Exchange exchange;
    private int quantity;
    private double price;
    private String userId;
    private OrderStatus status;
}
