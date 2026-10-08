package com.bright.auth_service.service;

import com.bright.auth_service.dto.UserRequest;
import com.bright.auth_service.entity.UserEntity;
import com.bright.auth_service.exception.ResourceNotFoundException;
import com.bright.auth_service.record.UserRecord;
import com.bright.auth_service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({UserService.class, MapperService.class})
@Testcontainers
public class UserServiceJpaTest {
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
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private UserRepository repository;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private String email = "email@example.com";
    private String name = "name";
    private String password = "very long and good password";

    UserRequest userRequest = new UserRequest(email, name, password);

    // getUserById
    @Test
    void getExistedUserById() {
        UserRecord createdUser = userService.createUser(email, name, password);

        UserRecord userRecord = userService.getUserById(createdUser.id());

        assertEquals(createdUser.id(), userRecord.id());
        assertEquals(createdUser.email(), userRecord.email());
    }

    @Test
    void getNotExistedUserById() {
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(UUID.randomUUID()));
    }

    // createUser
    @Test
    void createUser() {
        List<UserEntity> users = repository.findAll();
        assertEquals(0, users.size());

        UserRecord createdUser = userService.createUser(email, name, password);

        Optional<UserEntity> foundUser = repository.findById(createdUser.id());

        assertEquals(name, foundUser.get().getName());
        assertEquals(email, foundUser.get().getEmail());
    }

    // updateUser
    @Test
    void updateExistedUser() {
        UserRecord createdUser = userService.createUser(email, name, password);
        Optional<UserEntity> userEntity = repository.findById(createdUser.id());

        assertEquals(email, userEntity.get().getEmail());

        String newEmail = "new@email.com";
        userRequest.setEmail(newEmail);

        userService.updateUser(userEntity.get().getId(), userRequest);

        userEntity = repository.findById(createdUser.id());

        assertEquals(newEmail, userEntity.get().getEmail());
    }

    @Test
    void updateNotExistedUser() {
        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(UUID.randomUUID(), userRequest));
    }
}
