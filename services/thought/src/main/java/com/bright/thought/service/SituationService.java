package com.bright.thought.service;

import com.bright.thought.dto.SituationRequest;
import com.bright.thought.entity.SituationEntity;
import com.bright.thought.entity.ThoughtEntity;
import com.bright.thought.exception.ResourceNotFoundException;
import com.bright.thought.repository.SituationRepository;
import com.bright.thought.repository.ThoughtRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
        SituationEntity savedSituation = situationRepository.save(situation);

        situationToCreate.getThoughts()
                .stream()
                .filter(t -> t.getCriticalContent() != null && !t.getCriticalContent().isBlank())
                .forEach(thoughtRequest -> {
            ThoughtEntity thoughtEntity = new ThoughtEntity(
                    savedSituation,
                    thoughtRequest.getCriticalContent(),
                    thoughtRequest.getRealContent()
            );
            ThoughtEntity savedThought = thoughtRepository.save(thoughtEntity);
            savedSituation.addThought(savedThought);
        });

        return mapper.toSituationRequest(savedSituation);
    }

    @Transactional
    public SituationRequest updateSituation(UUID id, SituationRequest situationRequest) {
        SituationEntity situation = situationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Situation", id));

        situation.setDescription(situationRequest.getDescription());

        situationRequest.getThoughts().stream().forEach(thoughtRequest -> {
            if (thoughtRequest.getId() == null) {
                ThoughtEntity thoughtEntity = new ThoughtEntity(
                        situation,
                        thoughtRequest.getCriticalContent(),
                        thoughtRequest.getRealContent()
                );

                ThoughtEntity savedThought = thoughtRepository.save(thoughtEntity);
                situation.addThought(savedThought);
            } else {
                ThoughtEntity thoughtEntity = thoughtRepository.findById(thoughtRequest.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Thought", thoughtRequest.getId()));

                thoughtEntity.setCriticalContent(thoughtRequest.getCriticalContent());
                thoughtEntity.setRealContent(thoughtRequest.getRealContent());

                thoughtRepository.save(thoughtEntity);
            }
        });

        situationRepository.save(situation);

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
