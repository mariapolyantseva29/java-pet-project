package com.bright.thought.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThoughtEntityTest {
    UUID id = UUID.randomUUID();
    SituationEntity situation = new SituationEntity();
    String criticalContent = "critical content";
    String realContent = "real content";
    LocalDateTime date = LocalDateTime.now();

    @Test
    void operationsWithGettersAndSetters() {
        ThoughtEntity thought = new ThoughtEntity(situation, criticalContent, realContent);
        thought.setId(id);
        thought.setUpdateDate(date);
        thought.setCreateDate(date);

        assertEquals(id, thought.getId());
        assertEquals(situation, thought.getSituation());
        assertEquals(criticalContent, thought.getCriticalContent());
        assertEquals(realContent, thought.getRealContent());
        assertEquals(date, thought.getCreateDate());
        assertEquals(date, thought.getUpdateDate());

        thought.setCriticalContent(realContent);

        assertEquals(realContent, thought.getCriticalContent());
    }
}
