package com.bright.thought.service;

import com.bright.thought.dto.SituationRequest;
import com.bright.thought.dto.ThoughtRequest;
import com.bright.thought.entity.SituationEntity;
import com.bright.thought.entity.ThoughtEntity;
import com.bright.thought.exception.ResourceNotFoundException;
import com.bright.thought.repository.SituationRepository;
import com.bright.thought.repository.ThoughtRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({SituationService.class, MapperService.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class SituationServiceJpaTest {
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
    }

    @Autowired
    private ThoughtRepository thoughtRepository;
    @Autowired
    private SituationRepository situationRepository;
    @Autowired
    private SituationService serviceService;

    UUID userId = UUID.randomUUID();
    private String password = "very long and good password";

    // getAllSituations
    @Test
    void getAllSituations() {
        int amountOfSituation = 2;
        SituationEntity situation = new SituationEntity(userId, "Description of situation");
        SituationEntity situation2 = new SituationEntity(userId, "Description of another situation");

        situationRepository.save(situation);
        situationRepository.save(situation2);

        List<SituationEntity> situationEntities = situationRepository.findAll();

        assertEquals(amountOfSituation, situationEntities.size());

        List<SituationRequest> list = serviceService.getAllSituations(userId);

        assertEquals(list.get(0).getDescription(), situation.getDescription());
        assertEquals(list.get(1).getDescription(), situation2.getDescription());
        assertEquals(amountOfSituation, list.size());
    }

    // getEditedSituations
    @Test
    void returnNoDoneSituations() {
        SituationEntity situation = new SituationEntity(userId, "Done situation with thoughts");
        SituationEntity finalSituation  = situationRepository.save(situation);

        ThoughtEntity thoughtEntity = new ThoughtEntity(finalSituation, "critical content", "real content");
        thoughtRepository.save(thoughtEntity);

        List<SituationRequest> editedSituations = serviceService.getEditedSituations(userId);

        boolean hasDoneSituation = editedSituations.stream().anyMatch(situationRequest -> situationRequest.getId() == finalSituation.getId());

        assertFalse(hasDoneSituation);
        assertEquals(0, editedSituations.size());
    }

    @Test
    void returnSituationWithNoThoughts() {
        SituationEntity situation2 = new SituationEntity(userId, "Situation with no thoughts");
        SituationEntity finalSituation2 = situationRepository.save(situation2);

        List<SituationRequest> editedSituations = serviceService.getEditedSituations(userId);

        boolean hasEmptySituation = editedSituations.stream().anyMatch(situationRequest -> situationRequest.getId() == finalSituation2.getId());
        assertTrue(hasEmptySituation);
        assertEquals(1, editedSituations.size());
    }

    @Test
    void returnSituationsWithDoneAndEditedThoughts() {
        SituationEntity situation3 = new SituationEntity(userId, "Situation with one edited thought and one done thought");
        SituationEntity finalSituation3 = situationRepository.save(situation3);

        ThoughtEntity thoughtEntity2 = new ThoughtEntity(finalSituation3, "critical content", "");
        ThoughtEntity thoughtEntity3 = new ThoughtEntity(finalSituation3, "critical content", "real content");
        thoughtRepository.save(thoughtEntity2);
        thoughtRepository.save(thoughtEntity3);

        List<SituationRequest> editedSituations = serviceService.getEditedSituations(userId);

        boolean hasEditedAndDoneSituation = editedSituations.stream().anyMatch(situationRequest -> situationRequest.getId() == finalSituation3.getId());
        assertTrue(hasEditedAndDoneSituation);
        assertEquals(1, editedSituations.size());
    }

    @Test
    void returnSituationsWithAllEditedThoughts() {
        SituationEntity situation4 = new SituationEntity(userId, "Situation with edited thoughts");
        SituationEntity finalSituation4 = situationRepository.save(situation4);

        ThoughtEntity thoughtEntity4 = new ThoughtEntity(finalSituation4, "critical content", "");
        thoughtRepository.save(thoughtEntity4);

        List<SituationRequest> editedSituations = serviceService.getEditedSituations(userId);

        boolean hasOnlyEditedSituation = editedSituations.stream().anyMatch(situationRequest -> situationRequest.getId() == finalSituation4.getId());
        assertTrue(hasOnlyEditedSituation);
        assertEquals(1, editedSituations.size());
    }

    //getDoneSituations
    @Test
    void returnAllDoneSituations() {
        SituationEntity situation = new SituationEntity(userId, "Done situation with thoughts");
        SituationEntity finalSituation  = situationRepository.save(situation);

        ThoughtEntity thoughtEntity = new ThoughtEntity(finalSituation, "critical content", "real content");
        thoughtRepository.save(thoughtEntity);

        List<SituationRequest> editedSituations = serviceService.getDoneSituations(userId);

        boolean hasDoneSituation = editedSituations.stream().anyMatch(situationRequest -> situationRequest.getId() == finalSituation.getId());

        assertTrue(hasDoneSituation);
        assertEquals(1, editedSituations.size());
    }

    @Test
    void returnNoSituationsWithAllEditedThoughts() {
        SituationEntity situation = new SituationEntity(userId, "Situation with edited thoughts");
        SituationEntity finalSituation = situationRepository.save(situation);

        ThoughtEntity thoughtEntity = new ThoughtEntity(finalSituation, "critical content", "");
        thoughtRepository.save(thoughtEntity);

        List<SituationRequest> doneSituations = serviceService.getDoneSituations(userId);

        boolean hasOnlyEditedSituation = doneSituations.stream().anyMatch(situationRequest -> situationRequest.getId() == finalSituation.getId());
        assertFalse(hasOnlyEditedSituation);
        assertEquals(0, doneSituations.size());
    }

    @Test
    void returnNoSituationsWithNoThoughts() {
        SituationEntity situation = new SituationEntity(userId, "Situation with no thoughts");
        SituationEntity finalSituation = situationRepository.save(situation);

        List<SituationRequest> doneSituations = serviceService.getDoneSituations(userId);

        boolean hasEmptySituation = doneSituations.stream().anyMatch(situationRequest -> situationRequest.getId() == finalSituation.getId());
        assertFalse(hasEmptySituation);
        assertEquals(0, doneSituations.size());
    }

    @Test
    void returnNoSituationsWithDoneAndEditedThoughts() {
        SituationEntity situation = new SituationEntity(userId, "Situation with one edited thought and one done thought");
        SituationEntity finalSituation = situationRepository.save(situation);

        ThoughtEntity thoughtEntity = new ThoughtEntity(finalSituation, "critical content", "");
        ThoughtEntity thoughtEntity2 = new ThoughtEntity(finalSituation, "critical content", "real content");
        thoughtRepository.save(thoughtEntity);
        thoughtRepository.save(thoughtEntity2);

        List<SituationRequest> doneSituations = serviceService.getDoneSituations(userId);

        boolean hasEditedAndDoneSituation = doneSituations.stream().anyMatch(situationRequest -> situationRequest.getId() == finalSituation.getId());
        assertFalse(hasEditedAndDoneSituation);
        assertEquals(0, doneSituations.size());
    }

    // getSituationById
    @Test
    void whenThereIsSearchedSituation() {
        SituationEntity firstSituation = new SituationEntity(userId, "Situation with done thoughts");
        SituationEntity savedFirstSituation = situationRepository.save(firstSituation);

        SituationEntity secondSituation = new SituationEntity(userId, "Situation with done thoughts");
        situationRepository.save(secondSituation);

        SituationRequest foundSituation = serviceService.getSituationById(savedFirstSituation.getId());

        assertEquals(foundSituation.getId(), savedFirstSituation.getId());
    }

    @Test
    void whenThereIsNoSearchedSituation() {
        assertThrows(ResourceNotFoundException.class, () -> serviceService.getSituationById(UUID.randomUUID()));
    }

    // createSituation
    @Test
    void createSituationWithValidData() {
        String description = "Description";
        ThoughtRequest thought = new ThoughtRequest(null, "Critical content", "Real content");
        List<ThoughtRequest> thoughts = new ArrayList<>(List.of(thought));
        SituationRequest request = new SituationRequest(null, description, thoughts);

        SituationRequest created = serviceService.createSituation(userId, request);

        assertEquals(description, created.getDescription());
        assertEquals(1, created.getThoughts().size());
        assertEquals("Real content", created.getThoughts().get(0).getRealContent());
    }

    @Test
    void createSituationWithEmptyCriticalContent() {
        String description = "Description";
        ThoughtRequest thought = new ThoughtRequest(null, "", "");
        List<ThoughtRequest> thoughts = new ArrayList<>(List.of(thought));
        SituationRequest request = new SituationRequest(null, description, thoughts);

        SituationRequest created = serviceService.createSituation(userId, request);

        assertEquals(description, created.getDescription());
        assertEquals(0, created.getThoughts().size());
    }

    @Test
    void createSituationWithEmptyThought() {
        String description = "Description";
        List<ThoughtRequest> thoughts = new ArrayList<>();
        SituationRequest request = new SituationRequest(null, description, thoughts);

        SituationRequest created = serviceService.createSituation(userId, request);

        assertEquals(description, created.getDescription());
        assertEquals(0, created.getThoughts().size());
    }

    // updateSituation
    @Test
    void updateSituationDescription() {
        String description = "Description";
        String newDescription = "New description";
        List<ThoughtRequest> thoughts = new ArrayList<>();
        SituationRequest request = new SituationRequest(null, description, thoughts);

        SituationRequest created = serviceService.createSituation(userId, request);

        SituationRequest requestForUpdate = new SituationRequest(created.getId(), newDescription, thoughts);

        SituationRequest updated = serviceService.updateSituation(created.getId(), requestForUpdate);

        assertEquals(created.getId(), updated.getId());
        assertEquals(newDescription, updated.getDescription());
    }

    @Test
    void updateSituationThought() {
        String description = "Description";
        ThoughtRequest thought = new ThoughtRequest(null, "Critical content", "Real content");
        List<ThoughtRequest> thoughts = new ArrayList<>(List.of(thought));
        SituationRequest request = new SituationRequest(null, description, thoughts);

        SituationRequest created = serviceService.createSituation(userId, request);

        String newCriticalContent = "New critical content";
        thought = created.getThoughts().get(0);
        thought.setCriticalContent(newCriticalContent);
        thoughts = new ArrayList<>(List.of(thought));

        SituationRequest requestForUpdate = new SituationRequest(created.getId(), description, thoughts);

        SituationRequest updated = serviceService.updateSituation(created.getId(), requestForUpdate);

        assertEquals(1, updated.getThoughts().size());
        assertEquals(newCriticalContent, updated.getThoughts().get(0).getCriticalContent());
    }

    @Test
    void updateSituationWithNewThought() {
        String description = "Description";
        ThoughtRequest thought = new ThoughtRequest(null, "Critical content", "Real content");
        List<ThoughtRequest> thoughts = new ArrayList<>(List.of(thought));
        SituationRequest request = new SituationRequest(null, description, thoughts);

        SituationRequest created = serviceService.createSituation(userId, request);

        String newCriticalContent = "New critical content";
        ThoughtRequest newThought = new ThoughtRequest(null, newCriticalContent, "Real content");
        thoughts = new ArrayList<>(List.of(newThought));

        SituationRequest requestForUpdate = new SituationRequest(created.getId(), description, thoughts);

        SituationRequest updated = serviceService.updateSituation(created.getId(), requestForUpdate);

        assertEquals(2, updated.getThoughts().size());
    }

    @Test
    void updateSituationWithWrongThought() {
        String description = "Description";
        ThoughtRequest thought = new ThoughtRequest(null, "Critical content", "Real content");
        List<ThoughtRequest> thoughts = new ArrayList<>(List.of(thought));
        SituationRequest request = new SituationRequest(null, description, thoughts);

        SituationRequest created = serviceService.createSituation(userId, request);

        String newCriticalContent = "New critical content";
        thought = created.getThoughts().get(0);
        thought.setId(UUID.randomUUID());
        thought.setCriticalContent(newCriticalContent);
        thoughts = new ArrayList<>(List.of(thought));

        SituationRequest requestForUpdate = new SituationRequest(created.getId(), description, thoughts);

        assertThrows(ResourceNotFoundException.class, () -> serviceService.updateSituation(created.getId(), requestForUpdate));
    }

    // deleteSituation
    @Test
    void deleteNotExistingSituation() {
        assertThrows(ResourceNotFoundException.class, () -> serviceService.deleteSituation(UUID.randomUUID()));
    }

    @Test
    void deleteExistingSituation() {
        List<ThoughtRequest> thoughts = new ArrayList<>();
        SituationRequest request = new SituationRequest(null, "Description", thoughts);

        SituationRequest created = serviceService.createSituation(userId, request);
        assertEquals(1, situationRepository.findAll().size());

        serviceService.deleteSituation(created.getId());

        assertEquals(0, situationRepository.findAll().size());
    }
}
