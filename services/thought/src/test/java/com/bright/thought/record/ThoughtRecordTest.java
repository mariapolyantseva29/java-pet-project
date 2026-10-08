package com.bright.thought.record;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ThoughtRecordTest {
    UUID id = UUID.randomUUID();
    UUID situationId = UUID.randomUUID();
    String criticalContent = "Critical content about situation";
    String realContent = "Real content about situation";
    LocalDateTime createDate = LocalDateTime.of(2026, 4, 30, 12, 43);
    LocalDateTime updateDate = LocalDateTime.of(2026, 4, 30, 13, 21);

    ThoughtRecord thoughtRecord = new ThoughtRecord(id, situationId, criticalContent, realContent, createDate, updateDate);
    ThoughtRecord emptyThought = new ThoughtRecord(null, null, null, null, null, null);

    @Test
    void canAccessToAllColumns() {
        assertEquals(id, thoughtRecord.id());
        assertEquals(situationId, thoughtRecord.situationId());
        assertEquals(criticalContent, thoughtRecord.criticalContent());
        assertEquals(realContent, thoughtRecord.realContent());
        assertEquals(createDate, thoughtRecord.createDate());
        assertEquals(updateDate, thoughtRecord.updateDate());
    }

    @Test
    void accessedNotAddedValue() {
        assertNull(emptyThought.id());
        assertNull(emptyThought.situationId());
        assertNull(emptyThought.criticalContent());
        assertNull(emptyThought.realContent());
        assertNull(emptyThought.createDate());
        assertNull(emptyThought.updateDate());
    }

    @Test
    void compareTwoDifferentRecords() {
        assertFalse(thoughtRecord.equals(emptyThought));
    }

    @Test
    void compareTwoSameRecords() {
        ThoughtRecord thoughtRecord2 = new ThoughtRecord(id, situationId, criticalContent, realContent, createDate, updateDate);

        assertTrue(thoughtRecord.equals(thoughtRecord2));
    }
}
