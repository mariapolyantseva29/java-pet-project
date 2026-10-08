package com.bright.thought.repository;

import com.bright.thought.entity.SituationEntity;
import com.bright.thought.entity.ThoughtEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class SituationRepositoryTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test_thought_db")
            .withUsername("test_user")
            .withPassword("test_pass");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("spring.liquibase.enabled", () -> "false");
    }

    @Autowired
    SituationRepository situationRepository;
    @Autowired
    ThoughtRepository thoughtRepository;

    UUID userId = UUID.randomUUID();
    String description = "description";
    String password = "very long and good password";

    // getEditedSituations
    @Test
    void getSituationWithNoThoughtsForEdited() {
        SituationEntity situationWithNoThought = new SituationEntity(userId, description);
        SituationEntity savedSituationWithNoThought = situationRepository.save(situationWithNoThought);

        List<SituationEntity> situationEntities = situationRepository.getEditedSituations(userId);

        assertEquals(1, situationEntities.size());
        assertTrue(situationEntities.stream().anyMatch(s -> s.getId() == savedSituationWithNoThought.getId()));
    }

    @Test
    void getSituationWithEditedThoughtForEdited() {
        SituationEntity situationWithEditedThought = new SituationEntity(userId, description);
        SituationEntity savedSituationWithEditedThought = situationRepository.save(situationWithEditedThought);
        ThoughtEntity thoughtEntity = new ThoughtEntity(savedSituationWithEditedThought, "critical content", "");
        thoughtRepository.save(thoughtEntity);

        List<SituationEntity> situationEntities = situationRepository.getEditedSituations(userId);

        assertEquals(1, situationEntities.size());
        assertTrue(situationEntities.stream().anyMatch(s -> s.getId() == savedSituationWithEditedThought.getId()));
    }

    @Test
    void getHalfDoneSituationForEdited() {
        SituationEntity halfDoneSituation = new SituationEntity(userId, description);
        SituationEntity savedHalfDoneSituation = situationRepository.save(halfDoneSituation);
        ThoughtEntity thoughtEntity2 = new ThoughtEntity(savedHalfDoneSituation, "critical content", "real content");
        thoughtRepository.save(thoughtEntity2);
        ThoughtEntity thoughtEntity3 = new ThoughtEntity(savedHalfDoneSituation, "critical content", "");
        thoughtRepository.save(thoughtEntity3);

        List<SituationEntity> situationEntities = situationRepository.getEditedSituations(userId);

        assertEquals(1, situationEntities.size());
        assertTrue(situationEntities.stream().anyMatch(s -> s.getId() == savedHalfDoneSituation.getId()));
    }

    @Test
    void getDoneSituationForEdited() {
        SituationEntity doneSituation = new SituationEntity(userId, description);
        SituationEntity savedDoneSituation = situationRepository.save(doneSituation);
        ThoughtEntity thoughtEntity4 = new ThoughtEntity(savedDoneSituation, "critical content", "real content");
        thoughtRepository.save(thoughtEntity4);

        List<SituationEntity> situationEntities = situationRepository.getEditedSituations(userId);

        assertEquals(0, situationEntities.size());
    }

    // getDoneSituations
    @Test
    void getSituationWithNoThoughtsForDone() {
        SituationEntity situationWithNoThought = new SituationEntity(userId, description);

        List<SituationEntity> situationEntities = situationRepository.getDoneSituations(userId);

        assertEquals(0, situationEntities.size());
    }

    @Test
    void getSituationWithEditedThoughtForDone() {
        SituationEntity situationWithEditedThought = new SituationEntity(userId, description);
        SituationEntity savedSituationWithEditedThought = situationRepository.save(situationWithEditedThought);
        ThoughtEntity thoughtEntity = new ThoughtEntity(savedSituationWithEditedThought, "critical content", "");
        thoughtRepository.save(thoughtEntity);

        List<SituationEntity> situationEntities = situationRepository.getDoneSituations(userId);

        assertEquals(0, situationEntities.size());
    }

    @Test
    void getHalfDoneSituationForDone() {
        SituationEntity halfDoneSituation = new SituationEntity(userId, description);
        SituationEntity savedHalfDoneSituation = situationRepository.save(halfDoneSituation);
        ThoughtEntity thoughtEntity2 = new ThoughtEntity(savedHalfDoneSituation, "critical content", "real content");
        thoughtRepository.save(thoughtEntity2);
        ThoughtEntity thoughtEntity3 = new ThoughtEntity(savedHalfDoneSituation, "critical content", "");
        thoughtRepository.save(thoughtEntity3);

        List<SituationEntity> situationEntities = situationRepository.getDoneSituations(userId);

        assertEquals(0, situationEntities.size());
    }

    @Test
    void getDoneSituationForDone() {
        SituationEntity doneSituation = new SituationEntity(userId, description);
        SituationEntity savedDoneSituation = situationRepository.save(doneSituation);
        ThoughtEntity thoughtEntity4 = new ThoughtEntity(savedDoneSituation, "critical content", "real content");
        thoughtRepository.save(thoughtEntity4);

        List<SituationEntity> situationEntities = situationRepository.getDoneSituations(userId);

        assertEquals(1, situationEntities.size());
        assertTrue(situationEntities.stream().anyMatch(s -> s.getId() == savedDoneSituation.getId()));
    }
}
