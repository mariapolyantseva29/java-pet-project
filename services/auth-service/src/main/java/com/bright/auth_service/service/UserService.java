package com.bright.auth_service.service;

import com.bright.auth_service.dto.UserRequest;
import com.bright.auth_service.entity.UserEntity;
import com.bright.auth_service.exception.ResourceNotFoundException;
import com.bright.auth_service.record.UserRecord;
import com.bright.auth_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository repository;
    private final MapperService mapperService;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository repository,
            MapperService mapperService,
            PasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.mapperService = mapperService;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean userExist(String email) {
        return repository.existsByEmail(email);
    }

    public UserRecord getUserByEmail(String email) {
        UserEntity userEntity = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return mapperService.toUserRecord(userEntity);
    }

    public UserRecord getUserById(UUID id) {
        UserEntity userEntity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        return mapperService.toUserRecord(userEntity);
    }

    @Transactional
    public UserRecord createUser(String email, String name, String password) {
        if (userExist(email)) {
            throw new IllegalArgumentException("User already exists");
        }
        String encodedPassword = passwordEncoder.encode(password);

        UserEntity userEntity = new UserEntity(email, name, encodedPassword);

        repository.save(userEntity);

        return mapperService.toUserRecord(userEntity);
    }

    @Transactional
    public UserRecord updateUser(UUID id, UserRequest userToUpdate) {
        UserEntity userEntity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        userEntity.setName(userToUpdate.getName());
        userEntity.setEmail(userToUpdate.getEmail());

        return mapperService.toUserRecord(userEntity);
    }
}
