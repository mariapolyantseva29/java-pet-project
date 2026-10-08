package com.bright.auth_service.record;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserRecordTest {
    UUID id = UUID.randomUUID();
    String email = "email@example.com";
    String name = "John Kim";
    String password = "very long and good password";
    LocalDateTime createDate = LocalDateTime.of(2026, 12, 30, 23, 14);
    LocalDateTime updateDate = LocalDateTime.of(2026, 12, 30, 23, 14);

    UserRecord userRecord = new UserRecord(id, email, name, password, createDate, updateDate);

    @Test
    void canAccessAllColumns() {
        assertEquals(id, userRecord.id());
        assertEquals(email, userRecord.email());
        assertEquals(name, userRecord.name());
        assertEquals(password, userRecord.password());
        assertEquals(createDate, userRecord.createDate());
        assertEquals(updateDate, userRecord.updateDate());
    }

    @Test
    void accessedNotAddedValue() {
        UserRecord emptyUser = new UserRecord(null, null, null, null, null, null);

        assertNull(emptyUser.id());
        assertNull(emptyUser.email());
        assertNull(emptyUser.name());
        assertNull(emptyUser.createDate());
        assertNull(emptyUser.updateDate());
    }

    @Test
    void compareTwoSameRecords() {
        UserRecord userRecord2 = new UserRecord(id, email, name, password, createDate, updateDate);

        assertTrue(userRecord.equals(userRecord2));
    }

    @Test
    void compareTwoDifferentRecords() {
        String anotherEmail = "another@example.com";
        UserRecord userRecord2 = new UserRecord(id, anotherEmail, name, password, createDate, updateDate);

        assertFalse(userRecord.equals(userRecord2));
    }
}
