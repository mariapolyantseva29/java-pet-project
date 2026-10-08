package com.bright.auth_service.service;

import com.bright.auth_service.entity.UserEntity;
import com.bright.auth_service.record.UserRecord;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapperServiceTest {
    private MapperService mapperService = new MapperService();

    UUID id = UUID.randomUUID();
    String email = "user@email.com";
    String name = "userName";
    String password = "very long and good password";
    LocalDateTime created = LocalDateTime.now();
    LocalDateTime updated = LocalDateTime.now();

    @Test
    void toUserRecord() {
        UserEntity entity = new UserEntity(email, name, password);
        entity.setId(id);
        entity.setCreateDate(created);
        entity.setUpdateDate(updated);

        UserRecord record = mapperService.toUserRecord(entity);

        assertEquals(id, record.id());
        assertEquals(email, record.email());
        assertEquals(name, record.name());
        assertEquals(password, record.password());
        assertEquals(created, record.createDate());
        assertEquals(updated, record.updateDate());
    }
}
