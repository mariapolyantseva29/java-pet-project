package com.bright.thought.dto;

public class LogMessageDto {
    private String serviceName;
    private String logLevel;
    private String message;
    private String userId;
    private String traceId;
    private String timestamp;

    public LogMessageDto() {}
    public LogMessageDto(String serviceName, String logLevel, String message, String userId, String traceId, String timestamp) {
        this.serviceName = serviceName;
        this.logLevel = logLevel;
        this.message = message;
        this.userId = userId;
        this.traceId = traceId;
        this.timestamp = timestamp;
    }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public String getLogLevel() { return logLevel; }
    public void setLogLevel(String logLevel) { this.logLevel = logLevel; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTraceId() { return traceId; }
    public void setTraceId(String traceId) { this.traceId = traceId; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
