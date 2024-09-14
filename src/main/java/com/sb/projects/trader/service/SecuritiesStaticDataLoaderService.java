package com.sb.projects.trader.service;

import com.sb.projects.trader.DTO.SecurityDTO;
import com.sb.projects.trader.enums.ErrorCode;
import com.sb.projects.trader.enums.InstrumentType;
import com.sb.projects.trader.exceptions.BaseTraderException;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ObjectUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class SecuritiesStaticDataLoaderService implements StaticDataLoaderService {

    private StaticDataService staticDataService;

    @Override
    @PostConstruct
    public void Load() throws BaseTraderException {

        List<SecurityDTO> securities = new ArrayList<>();
        InputStream is = null;

        try {
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource("classpath:static/etf_security_master.csv");

            is = resource.getInputStream();
            try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                 CSVParser csvParser = new CSVParser(fileReader,
                         CSVFormat.DEFAULT
                                 .withFirstRecordAsHeader()
                                 .withIgnoreHeaderCase()
                                 .withTrim())) {


                Iterable<CSVRecord> csvRecords = csvParser.getRecords();

                for (CSVRecord csvRecord : csvRecords) {
                    SecurityDTO security = SecurityDTO.builder()
                            .id(csvRecord.get(0))
                            .symbol(csvRecord.get(1))
                            .name(csvRecord.get(2))
                            .series(csvRecord.get(3))
                            .tickSize(csvRecord.get(4))
                            .lotSize(csvRecord.get(5))
                            .instrumentType(InstrumentType.ETF)
                            .segment(csvRecord.get(7))
                            .exchange(csvRecord.get(8))
                            .upperLimit(csvRecord.get(9))
                            .lowerLimit(csvRecord.get(10))
                            .expiryDate(csvRecord.get(11))
                            .strikePrice(csvRecord.get(12))
                            .freezeQuantity(csvRecord.get(13)).build();

                    securities.add(security);
                }
            }
        } catch (IOException e) {
            throw new BaseTraderException(ErrorCode.IOError, "Master CSV file not found", e);
        } finally {
            try {
                if (!ObjectUtils.isEmpty(is))
                    is.close();
            } catch (IOException e) {
                throw new BaseTraderException(ErrorCode.IOError, "Master CSV file not found", e);
            }
        }

        staticDataService.saveAll(securities);
    }
}
