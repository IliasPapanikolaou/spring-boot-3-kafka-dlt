package com.ipap.springboot3kafkadlt.controller;

import com.ipap.springboot3kafkadlt.dto.UserDto;
import com.ipap.springboot3kafkadlt.publisher.KafkaMessagePublisher;
import com.ipap.springboot3kafkadlt.util.CsvReaderUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/producer-app")
public class EventController {

    private final KafkaMessagePublisher publisher;

    @PostMapping("/publish-user")
    public ResponseEntity<HttpStatus> publishEvent(@RequestBody UserDto userDto) {
        publisher.sendEvents(userDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/publish-csv")
    public ResponseEntity<?> publicCsvEvent() {
        try {
            List<UserDto> userDtos = CsvReaderUtils.readDataFromCsv();
            assert userDtos != null;
            userDtos.forEach(publisher::sendEvents);
            return ResponseEntity.ok("Message published successfully");
        } catch (Exception exception) {
            return ResponseEntity.
                    status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

}
