package com.sb.projects.trader.exceptions;

import com.sb.projects.trader.DTO.BrokerErrorDTO;
import com.sb.projects.trader.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

@Getter
public class BrokerHttpException extends BaseTraderException{
    private final HttpStatus httpStatus;
    private final BrokerErrorDTO brokerErrorDTO;

    public BrokerHttpException(HttpStatus httpStatus,
                               ErrorCode errorCode,
                               String message, Throwable cause, BrokerErrorDTO brokerErrorDTO) {
        super(errorCode, message, cause);
        this.httpStatus = httpStatus;
        this.brokerErrorDTO = brokerErrorDTO;
    }
}
