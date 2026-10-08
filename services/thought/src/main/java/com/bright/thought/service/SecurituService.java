package com.bright.thought.service;

import com.bright.thought.repository.SituationRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("securityService")
public class SecurituService {
    private final SituationRepository repository;

    public SecurituService(SituationRepository repository) {
        this.repository = repository;
    }

    public boolean isUserOwner(UUID situationsId, UUID userId) {
        return repository.existsByIdAndUserId(situationsId, userId);
    }
}
