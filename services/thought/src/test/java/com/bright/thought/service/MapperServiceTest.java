package com.bright.thought.service;

import com.bright.thought.dto.SituationRequest;
import com.bright.thought.dto.ThoughtRequest;
import com.bright.thought.entity.SituationEntity;
import com.bright.thought.entity.ThoughtEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapperServiceTest {
    private MapperService mapperService = new MapperService();

    UUID id = UUID.randomUUID();
    LocalDateTime created = LocalDateTime.now();
    LocalDateTime updated = LocalDateTime.now();
    String description = "Description";
    String criticalContent = "Critical content";
    String realContent = "Real content";

    @Test
    void toThoughtRequest() {
        SituationEntity situation = new SituationEntity();
        ThoughtEntity thoughtEntity = new ThoughtEntity(situation, criticalContent, realContent);
        thoughtEntity.setId(id);
        thoughtEntity.setCreateDate(created);
        thoughtEntity.setUpdateDate(updated);

        ThoughtRequest request = mapperService.toThoughtRequest(thoughtEntity);

        assertEquals(id, request.getId());
        assertEquals(criticalContent, request.getCriticalContent());
        assertEquals(realContent, request.getRealContent());
        assertEquals(created, request.getCreateDate());
        assertEquals(updated, request.getUpdateDate());
    }

    @Test
    void toSituationRequest() {
        SituationEntity situationEntity = new SituationEntity(id, description);
        ThoughtEntity thoughtEntity = new ThoughtEntity(situationEntity, criticalContent, realContent);
        thoughtEntity.setId(id);
        situationEntity.addThought(thoughtEntity);

        SituationRequest situationRequest = mapperService.toSituationRequest(situationEntity);

        assertEquals(description, situationRequest.getDescription());
        assertEquals(id, situationRequest.getThoughts().get(0).getId());
    }
}
