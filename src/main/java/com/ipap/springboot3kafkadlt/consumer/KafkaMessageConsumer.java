package com.ipap.springboot3kafkadlt.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipap.springboot3kafkadlt.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class KafkaMessageConsumer {

    @RetryableTopic(
            attempts = "4",
            backoff = @Backoff(delay = 3000, multiplier = 1.5, maxDelay = 15000),
            exclude = {NullPointerException.class, RuntimeException.class}
    ) // topic naming convention: 3 topic N-1
    @KafkaListener(topics = "${app.topic.name}", groupId = "test-topic-x")
    public void consumeEvents(UserDto userDto, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Header(KafkaHeaders.OFFSET) long offset) {
        try {
            log.info("Received: {} from {} offset {}", new ObjectMapper().writeValueAsString(topic), topic, offset);
            // Validate restricted IP  before process the records
            List<String> restrictedIpList = List.of("32.241.244.236", "15.55.49.164", "81.1.95.253", "126.130.43.183");
            if (restrictedIpList.contains(userDto.getIpAddress())) {
                throw new RuntimeException("Invalid IP Address received!");
            }
        } catch (JsonProcessingException e) {
            log.error("Error during JsonProcessing: {}", e.getMessage());
        }
    }

    @DltHandler
    public void listenDLT(UserDto userDto, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Header(KafkaHeaders.OFFSET) long offset) {
        log.info("DLT Received : {} , from {} , offset {}", userDto.getFirstname(), topic, offset);
    }
}
