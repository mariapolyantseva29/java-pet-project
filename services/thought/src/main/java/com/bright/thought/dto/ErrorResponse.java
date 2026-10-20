package com.bright.thought.dto;

import java.time.LocalDateTime;

public class ErrorResponse {
    private int status;
    private String message;
    private LocalDateTime date;

    public ErrorResponse() {}
    public ErrorResponse(int status, String message, LocalDateTime date) {
        this.status = status;
        this.message = message;
        this.date = date;
    }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}
