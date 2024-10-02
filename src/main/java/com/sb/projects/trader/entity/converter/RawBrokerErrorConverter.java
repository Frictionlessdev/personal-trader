package com.sb.projects.trader.entity.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.projects.trader.DTO.BrokerErrorDTO;
import com.sb.projects.trader.enums.ErrorCode;
import com.sb.projects.trader.exceptions.BaseTraderException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Slf4j
@Converter(autoApply = true)
public class RawBrokerErrorConverter implements AttributeConverter<BrokerErrorDTO, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(BrokerErrorDTO brokerErrorDTO) {
        try {
            return ObjectUtils.isEmpty(brokerErrorDTO) ? "" :
                    objectMapper.writeValueAsString(brokerErrorDTO);
        } catch (JsonProcessingException e) {
            log.error("Error converting from broker error Dto");
            return "";
        }
    }

    @Override
    public BrokerErrorDTO convertToEntityAttribute(String s) {
        try {
            return StringUtils.isEmpty(s) ? null :
                    objectMapper.readValue(s, BrokerErrorDTO.class);
        } catch (JsonProcessingException e) {
            log.error("Error converting to broker error Dto");
            return null;
        }
    }
}
