package com.bright.thought.controller;

import com.bright.thought.dto.SituationRequest;
import com.bright.thought.dto.ThoughtRequest;
import com.bright.thought.security.AppUser;
import com.bright.thought.service.SituationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Transactional
public class SituationsControllerTest {
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
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.flyway.enabled", () -> "true");
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SituationService situationService;

    UUID user1Id = UUID.randomUUID();
    UUID user2Id = UUID.randomUUID();
    String description = "Description of situation";
    String email = "name@example.com";
    String password = "password";

    AppUser principal;

    ThoughtRequest emptyThought;
    ThoughtRequest editedThought;
    ThoughtRequest doneThought;

    SituationRequest emptySituation1;
    SituationRequest editedSituation1;
    SituationRequest doneSituation1;
    SituationRequest emptySituation2;
    SituationRequest editedSituation2;
    SituationRequest doneSituation2;

    @BeforeEach
    void envSetUp() {
        principal = new AppUser(user1Id, email, password, "ROLE_USER");

        emptyThought = new ThoughtRequest(null, "", "");
        editedThought = new ThoughtRequest(null, "First critical thought", "");
        doneThought = new ThoughtRequest(null, "First critical thought", "First real thought");

        SituationRequest emptySituation = new SituationRequest(null, description, List.of(emptyThought));
        SituationRequest editedSituation = new SituationRequest(null, description, List.of(editedThought));
        SituationRequest doneSituation = new SituationRequest(null, description, List.of(doneThought));

        // situations for user1
        emptySituation1 = situationService.createSituation(user1Id, emptySituation);
        editedSituation1 = situationService.createSituation(user1Id, editedSituation);
        doneSituation1 = situationService.createSituation(user1Id, doneSituation);
        // situations for user2
        emptySituation2 = situationService.createSituation(user2Id, emptySituation);
        editedSituation2 = situationService.createSituation(user2Id, editedSituation);
        doneSituation2 = situationService.createSituation(user2Id, doneSituation);
    }

    // getSituations
    @Test
    void getSituationsForCurrentUser() throws Exception {
        mockMvc.perform(get("/api/situations")
                .header("X-User-Id", user1Id)
                .with(authentication(
                        new UsernamePasswordAuthenticationToken(
                                principal,
                                null,
                                principal.getAuthorities()
                        )
                ))
        )
                .andExpect(status().isOk());
    }

    // getEditedSituations
    @Test
    void getEditedSituationsForCurrentUser() throws Exception {
        mockMvc.perform(get("/api/situations/edited")
                .header("X-User-Id", user1Id)
                .with(authentication(
                        new UsernamePasswordAuthenticationToken(
                                principal,
                                null,
                                principal.getAuthorities()
                        )
                ))
        )
                .andExpect(status().isOk());
    }

    // getDoneSituations
    @Test
    void getDoneSituationsForCurrentUser() throws Exception {
        mockMvc.perform(get("/api/situations/done")
                .header("X-User-Id", user1Id)
                .with(authentication(
                        new UsernamePasswordAuthenticationToken(
                                principal,
                                null,
                                principal.getAuthorities()
                        )
                ))
        )
                .andExpect(status().isOk());
    }

    // getSituation
    @Test
    void getSituationById() throws Exception {
        String url = "/api/situations/" + emptySituation1.getId();

        mockMvc.perform(get(url)
                .header("X-User-Id", user1Id)
                .with(authentication(
                        new UsernamePasswordAuthenticationToken(
                                principal,
                                null,
                                principal.getAuthorities()
                        )
                ))
        )
                .andExpect(status().isOk());
    }
}
