package com.ipap.springboot3kafkadlt.util;

import com.ipap.springboot3kafkadlt.dto.UserDto;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
public class CsvReaderUtils {

    public static List<UserDto> readDataFromCsv() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource("users.csv").getInputStream()))) {
            CsvToBean<UserDto> csvToBean = new CsvToBeanBuilder<UserDto>(reader)
                    .withType(UserDto.class)
                    .build();

            return csvToBean.parse();
        } catch (IOException e) {
            log.error("Error occurred during parsing: {}", e.getMessage());
            // Handle the exception as needed
            return null;
        }
    }
}
