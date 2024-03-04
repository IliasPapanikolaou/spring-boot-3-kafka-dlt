package com.ipap.springboot3kafkadlt.publisher;

import com.ipap.springboot3kafkadlt.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class KafkaMessagePublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.topic.name}")
    private String topicName;

    public KafkaMessagePublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvents(UserDto userDto) {
        try {
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicName, userDto);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Sent message=[{}] with offset=[{}]", userDto.toString(), result.getRecordMetadata().offset());
                } else {
                    log.warn("Unable to send message=[{}] due to: {}", userDto.toString(), ex.getMessage());
                }
            });
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
