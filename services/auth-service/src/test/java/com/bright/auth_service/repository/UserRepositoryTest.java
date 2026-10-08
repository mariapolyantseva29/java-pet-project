package com.bright.auth_service.repository;

import com.bright.auth_service.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class UserRepositoryTest {
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

        registry.add("spring.flyway.enabled", () -> "true");
    }

    @Autowired
    UserRepository userRepository;

    String email = "Email";
    String password = "very long and good password";

    // findByEmail
    @Test
    void getUserByRightEmail() {
        UserEntity user = new UserEntity(email, "Name", password);
        UserEntity savedUser = userRepository.save(user);

        Optional<UserEntity> foundUser = userRepository.findByEmail(email);
        assertEquals(email, foundUser.get().getEmail());
        assertEquals(savedUser.getId(), foundUser.get().getId());
    }

    @Test
    void getUserByWrongEmail() {
        Optional<UserEntity> foundUser = userRepository.findByEmail(email);
        assertThrows(NoSuchElementException.class, () -> foundUser.get());
    }
}
