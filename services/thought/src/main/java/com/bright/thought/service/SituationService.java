package com.bright.thought.service;

import com.bright.thought.dto.SituationRequest;
import com.bright.thought.dto.ThoughtRequest;
import com.bright.thought.entity.SituationEntity;
import com.bright.thought.entity.ThoughtEntity;
import com.bright.thought.exception.ResourceNotFoundException;
import com.bright.thought.repository.SituationRepository;
import com.bright.thought.repository.ThoughtRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SituationService {
    private final ThoughtRepository thoughtRepository;
    private final SituationRepository situationRepository;
    private final MapperService mapper;

    public SituationService(
            ThoughtRepository thoughtRepository,
            SituationRepository situationRepository,
            MapperService mapper
    ) {
        this.thoughtRepository = thoughtRepository;
        this.situationRepository = situationRepository;
        this.mapper = mapper;
    }

    public List<SituationRequest> getAllSituations(UUID userId) {
        List<SituationEntity> situationEntities = situationRepository.findByUserId(userId);

        return situationEntities.stream().map(mapper::toSituationRequest).toList();
    }

    public List<SituationRequest> getEditedSituations(UUID userId) {
        List<SituationEntity> editedSituations = situationRepository.getEditedSituations(userId);

        return editedSituations.stream().map(mapper::toSituationRequest).toList();
    }

    public List<SituationRequest> getDoneSituations(UUID userId) {
        List<SituationEntity> doneSituations = situationRepository.getDoneSituations(userId);

        return doneSituations.stream().map(mapper::toSituationRequest).toList();
    }

    public SituationRequest getSituationById(UUID id) {
        SituationEntity situationEntity = situationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Situation", id));

        return mapper.toSituationRequest(situationEntity);
    }

    @Transactional
    public SituationRequest createSituation(UUID userId,  SituationRequest situationToCreate) {
        SituationEntity situation = new SituationEntity(
                userId,
                situationToCreate.getDescription()
        );
        situationRepository.save(situation);

        List<ThoughtEntity> newThoughts = situationToCreate.getThoughts()
                .stream()
                .filter(t -> t.getCriticalContent() != null && !t.getCriticalContent().isBlank())
                .map(thoughtRequest -> new ThoughtEntity(
                    situation,
                    thoughtRequest.getCriticalContent(),
                    thoughtRequest.getRealContent()
                )).collect(Collectors.toList());

        thoughtRepository.saveAll(newThoughts);
        situation.setThoughts(newThoughts);

        return mapper.toSituationRequest(situation);
    }

    @Transactional
    public SituationRequest updateSituation(UUID id, SituationRequest situationRequest) {
        SituationEntity situation = situationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Situation", id));

        situation.setDescription(situationRequest.getDescription());

        List<ThoughtEntity> newThoughts = new ArrayList<>();
        List<ThoughtEntity> updatedThoughts = new ArrayList<>();

        situationRequest.getThoughts().stream().forEach(thoughtRequest -> {
            if (thoughtRequest.getId() == null) {
                newThoughts.add(new ThoughtEntity(
                        situation,
                        thoughtRequest.getCriticalContent(),
                        thoughtRequest.getRealContent()
                ));
            } else {
                ThoughtEntity thoughtEntity = thoughtRepository.findById(thoughtRequest.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Thought", thoughtRequest.getId()));

                thoughtEntity.setCriticalContent(thoughtRequest.getCriticalContent());
                thoughtEntity.setRealContent(thoughtRequest.getRealContent());

                updatedThoughts.add(thoughtEntity);
            }
        });

        if (!newThoughts.isEmpty()) {
            thoughtRepository.saveAll(newThoughts);
        }
        if (!updatedThoughts.isEmpty()) {
            thoughtRepository.saveAll(updatedThoughts);
        }
        List<ThoughtEntity> allThoughts = Stream.concat(newThoughts.stream(), updatedThoughts.stream())
                                                .collect(Collectors.toList());

        situation.setThoughts(allThoughts);

        return mapper.toSituationRequest(situation);
    }

    @Transactional
    public SituationRequest deleteSituation(UUID id) {
        SituationEntity situation = situationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Situation", id));

        situationRepository.deleteById(id);

        return mapper.toSituationRequest(situation);
    }
}
