package com.bright.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserRequest {
    private UUID id;

    @NotNull(message = "Email can not be null")
    @NotBlank(message = "Email can not be blank")
    @Email(message = "Incorrect email format")
    private String email;

    @NotNull(message = "Name can not be null")
    @NotBlank(message = "Name can not be blank")
    private String name;

    @NotNull(message = "Password can not be null")
    private String password;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    public UserRequest(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public UserRequest(UUID id, String email, String name, String password, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) { this.email = email; }

    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }
}
