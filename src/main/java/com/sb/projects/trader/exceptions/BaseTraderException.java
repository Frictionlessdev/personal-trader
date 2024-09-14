package com.sb.projects.trader.exceptions;

import com.sb.projects.trader.enums.ErrorCode;
import lombok.Getter;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
public class BaseTraderException extends Exception {

    private final ErrorCode errorCode;

    public BaseTraderException(ErrorCode errorCode, String message, Throwable cause){
        super(message, cause);
        this.errorCode = errorCode;
    }

}
