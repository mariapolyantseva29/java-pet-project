package com.bright.thought.service;

import com.bright.thought.dto.SituationRequest;
import com.bright.thought.dto.ThoughtRequest;
import com.bright.thought.entity.SituationEntity;
import com.bright.thought.entity.ThoughtEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapperService {

    public ThoughtRequest toThoughtRequest(ThoughtEntity thoughtEntity) {
        return new ThoughtRequest(
            thoughtEntity.getId(),
            thoughtEntity.getSituation().getId(),
            thoughtEntity.getCriticalContent(),
            thoughtEntity.getRealContent(),
            thoughtEntity.getCreateDate(),
            thoughtEntity.getUpdateDate()
        );
    }

    public SituationRequest toSituationRequest(SituationEntity situationEntity) {
        List<ThoughtRequest> thoughtRequestList = situationEntity
                .getThoughts()
                .stream()
                .map(this::toThoughtRequest)
                .toList();

        return new SituationRequest(
            situationEntity.getId(),
            situationEntity.getDescription(),
            thoughtRequestList
        );
    }
}
