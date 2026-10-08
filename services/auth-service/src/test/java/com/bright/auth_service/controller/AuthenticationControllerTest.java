package com.bright.auth_service.controller;

import com.bright.auth_service.security.AppUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Transactional
public class AuthenticationControllerTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test_auth_db")
            .withUsername("test_user")
            .withPassword("test_pass");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
        registry.add("spring.flyway.enabled", () -> "true");
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AppUserDetailsService userDetailsService;

    @Test
    void createJwtTokenForNotExistingUser() throws Exception {
        String url = "/api/auth/login";
        String email = "test@example.com";

        assertEquals(false, userDetailsService.existsByEmail(email));

        mockMvc.perform(post(url)
                .param("email", email)
                .param("password", "password123")
                .with(csrf())
        )
                .andExpect(cookie().exists("jwt_token"))
                .andExpect(cookie().httpOnly("jwt_token", true))
                .andExpect(cookie().secure("jwt_token", false))
                .andExpect(cookie().path("jwt_token", "/"))
                .andExpect(cookie().maxAge("jwt_token", 3600))
                .andExpect(status().isFound());

        assertEquals(true, userDetailsService.existsByEmail(email));
    }

    @Test
    void createJWTTokenForExistingUser() throws Exception {
        String url = "/api/auth/login";
        String email = "test@example.com";
        String password = "password123";

        userDetailsService.createUser(email, password);

        assertEquals(true, userDetailsService.existsByEmail(email));

        mockMvc.perform(post(url)
                        .param("email", email)
                        .param("password", "password123")
                        .with(csrf())
                )
                .andExpect(cookie().exists("jwt_token"))
                .andExpect(cookie().httpOnly("jwt_token", true))
                .andExpect(cookie().secure("jwt_token", false))
                .andExpect(cookie().path("jwt_token", "/"))
                .andExpect(cookie().maxAge("jwt_token", 3600))
                .andExpect(status().isFound());

        assertEquals(true, userDetailsService.existsByEmail(email));
    }
}
