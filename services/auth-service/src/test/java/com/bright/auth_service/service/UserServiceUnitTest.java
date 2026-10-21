package com.bright.auth_service.service;

import com.bright.auth_service.dto.UserRequest;
import com.bright.auth_service.entity.UserEntity;
import com.bright.auth_service.exception.ResourceNotFoundException;
import com.bright.auth_service.record.UserRecord;
import com.bright.auth_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {
    @Mock
    private UserRepository repository;
    @Mock
    private MapperService mapperService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UUID id = UUID.randomUUID();
    private String email = "email@example.com";
    private String name = "name";
    private String password = "very long and good password";
    private LocalDateTime createDate = LocalDateTime.now();
    private LocalDateTime updateDate = LocalDateTime.now();

    private UserEntity userEntity;
    private UserRecord userRecord;

    @BeforeEach
    void setUp() {
        when(passwordEncoder.encode(password)).thenReturn("mocked-hashed-value");

        userEntity = new UserEntity(email, name, password);
        userEntity.setId(id);
        String hashedPassword = passwordEncoder.encode(password);
        userRecord = new UserRecord(id, email, name, hashedPassword, createDate, updateDate);
    }

    // getUserById
    @Test
    void getUserByIdTest() {
        UserRecord userRecord = new UserRecord(id, email, name, password, createDate, updateDate);
        when(repository.findById(id)).thenReturn(Optional.ofNullable(userEntity));
        when(mapperService.toUserRecord(userEntity)).thenReturn(userRecord);

        assertEquals(userRecord, userService.getUserById(id));
    }

    @Test
    void thereNoUserGottenById() {
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(id));
    }

    // createUser
    @Test
    void createUser() {
        when(repository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(mapperService.toUserRecord(any(UserEntity.class))).thenReturn(userRecord);

        assertEquals(userRecord, userService.createUser(email, name, password));

        verify(repository, times(1)).save(any(UserEntity.class));
        verify(mapperService, times(1)).toUserRecord(any(UserEntity.class));
    }

    // updateUser
    @Test
    void updateUser() {
        LocalDateTime frozenTime = LocalDateTime.now();
        String newEmail = "new@email.com";
        UserRequest userToUpdate = new UserRequest(id, newEmail, password, name, createDate, updateDate);
        userEntity.setEmail(newEmail);
        UserRecord userRecord = new UserRecord(id, newEmail, name, password, createDate, frozenTime);

        try(MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class)) {
            when(LocalDateTime.now()).thenReturn(frozenTime);

            when(repository.findById(id)).thenReturn(Optional.of(userEntity));
            when(mapperService.toUserRecord(userEntity)).thenReturn(userRecord);
        }

        assertEquals(userRecord, userService.updateUser(id, userToUpdate));
    }

    @Test
    void updateNotFoundUser() {
        String newEmail = "new@email.com";
        UserRequest userToUpdate = new UserRequest(id, newEmail, name, password, createDate, updateDate);

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(id, userToUpdate));
    }
}
