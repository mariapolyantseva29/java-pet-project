package com.bright.logging_service.service;

import com.bright.logging_service.dto.LogMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class LogConsumerService {
    private static final Logger log = LoggerFactory.getLogger(LogConsumerService.class);

    @KafkaListener(topics = "app-logs-topic", groupId = "logging-group")
    public void consume(LogMessageDto logMessageDto) {
        String message = getMessage(logMessageDto);

        switch (logMessageDto.getLogLevel()) {
            case "INFO":
                log.info(message);
                break;
            case "DEBUG":
                log.debug(message);
                break;
            case "ERROR":
                log.error(message);
                break;
            case "WARN":
                log.warn(message);
                break;
            case "TRACE":
                log.trace(message);
                break;
            default:
                log.info(message);
        }
    }

    private String getMessage(LogMessageDto logMessageDto) {
        StringBuilder builder = new StringBuilder("Сервис: [")
                .append(logMessageDto.getServiceName())
                .append("] | Сообщение: ")
                .append(logMessageDto.getMessage())
                .append(" | UserId: ")
                .append(logMessageDto.getUserId())
                .append(" | TraceId: ")
                .append(logMessageDto.getTraceId())
                .append(" | Timestamp: ")
                .append(logMessageDto.getTimestamp());

        return builder.toString();
    }
}
