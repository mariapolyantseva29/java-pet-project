package com.bright.thought.record;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SituationRecordTest {
    UUID id = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    String description = "Description for situation";
    LocalDateTime createDate = LocalDateTime.of(2026, 5, 21, 12, 0);
    LocalDateTime updateDate = LocalDateTime.of(2020, 1, 1, 14, 32);

    SituationRecord situationRecord = new SituationRecord(id, userId, description, createDate, updateDate);

    @Test
    void canAccessAllColumns() {
        assertEquals(id, situationRecord.id());
        assertEquals(userId, situationRecord.userId());
        assertEquals(description, situationRecord.description());
        assertEquals(createDate, situationRecord.createDate());
        assertEquals(updateDate, situationRecord.updateDate());
    }

    @Test
    void accessedNotAddedValue() {
        SituationRecord emptySituation = new SituationRecord(null, null, null, null, null);

        assertNull(emptySituation.id());
        assertNull(emptySituation.userId());
        assertNull(emptySituation.description());
        assertNull(emptySituation.createDate());
        assertNull(emptySituation.updateDate());
    }

    @Test
    void compareTwoSameRecords() {
        SituationRecord situationRecord2 = new SituationRecord(id, userId, description, createDate, updateDate);

        assertTrue(situationRecord.equals(situationRecord2));
    }

    @Test
    void compareTwoDifferentRecords() {
        String anotherDescription = "Another description";
        SituationRecord situationRecord2 = new SituationRecord(id, userId, anotherDescription, createDate, updateDate);

        assertFalse(situationRecord.equals(situationRecord2));
    }
}
