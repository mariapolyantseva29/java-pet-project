package com.bright.auth_service.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "users")
@Entity
public class UserEntity {
    @Id
    @UuidGenerator
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    private String name;

    @Column(nullable = false)
    private String password;

    @Column(name = "create_date", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updateDate;

    public UserEntity() {}

    public UserEntity(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDateTime getCreateDate() { return createDate; }
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() { return updateDate; }
    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
}
