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
public class ThoughtRepositoryTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_pass");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.flyway.enabled", () -> "true");
    }

    @Autowired
    SituationRepository situationRepository;
    @Autowired
    ThoughtRepository thoughtRepository;

    UUID userId = UUID.randomUUID();
    String description = "description";
    String password = "very long and good password";

    // findBySituationId
    @Test
    void getThoughtsBySituationWithNoThought() {
        SituationEntity situationWithNoThought = new SituationEntity(userId, description);
        SituationEntity savedSituationWithNoThought = situationRepository.save(situationWithNoThought);

        SituationEntity situationWithEditedThought = new SituationEntity(userId, description);
        SituationEntity savedSituationWithEditedThought = situationRepository.save(situationWithEditedThought);
        ThoughtEntity thoughtEntity = new ThoughtEntity(savedSituationWithEditedThought, "critical content", "");
        thoughtRepository.save(thoughtEntity);

        List<ThoughtEntity> thoughts = thoughtRepository.findBySituationId(savedSituationWithNoThought.getId());

        assertEquals(0, thoughts.size());
    }

    @Test
    void getThoughtsBySituationWithThoughts() {
        SituationEntity situationWithEditedThought = new SituationEntity(userId, description);
        SituationEntity savedSituationWithEditedThought = situationRepository.save(situationWithEditedThought);
        ThoughtEntity thoughtEntity = new ThoughtEntity(savedSituationWithEditedThought, "critical content", "");
        thoughtRepository.save(thoughtEntity);

        List<ThoughtEntity> thoughts = thoughtRepository.findBySituationId(savedSituationWithEditedThought.getId());

        assertEquals(1, thoughts.size());
        assertTrue(thoughts.stream().anyMatch(t -> t.getId() == thoughtEntity.getId()));
    }
}
