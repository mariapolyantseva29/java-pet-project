package com.bright.thought.entity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SituationEntityTest {
    String description = "Description";
    UUID userId = UUID.randomUUID();

    @Test
    void operationWithId() {
        SituationEntity situation = new SituationEntity();

        assertEquals(null, situation.getId());
    }

    @Test
    void operationWithUser() {
        SituationEntity situation = new SituationEntity(userId, description);

        assertEquals(userId, situation.getUserId());
    }

    @Test
    void operationWithDescription() {
        SituationEntity situation = new SituationEntity(userId, description);

        assertEquals(description, situation.getDescription());

        String newDescription = "New description";
        situation.setDescription(newDescription);

        assertEquals(newDescription, situation.getDescription());
    }

    @Test
    void operationWithThought() {
        SituationEntity situation = new SituationEntity();
        assertEquals(0, situation.getThoughts().size());

        ThoughtEntity thought = new ThoughtEntity();
        situation.addThought(thought);

        assertEquals(1, situation.getThoughts().size());
        assertEquals(thought, situation.getThoughts().get(0));
    }
}
