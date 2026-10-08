package com.bright.thought.service;

import com.bright.thought.dto.LogMessageDto;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class KafkaLogProducer {
    private static final String TOPIC = "app-logs-topic";
    private static final String SERVICE_NAME = "thought-service";

    private final KafkaTemplate<String, LogMessageDto> kafkaTemplate;

    public KafkaLogProducer(KafkaTemplate<String, LogMessageDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendLog(String logLevel, String message) {
        String userId = MDC.get("userId");
        String traceId = MDC.get("traceId");
        String timestamp = Instant.now().toString();

        LogMessageDto log = new LogMessageDto(SERVICE_NAME, logLevel, message, userId, traceId, timestamp);

        kafkaTemplate.send(TOPIC, log);
    }
}
