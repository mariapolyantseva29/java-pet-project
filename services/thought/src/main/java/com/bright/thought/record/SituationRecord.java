package com.bright.thought.record;

import java.time.LocalDateTime;
import java.util.UUID;

public record SituationRecord(
        UUID id,
        UUID userId,
        String description,
        LocalDateTime createDate,
        LocalDateTime updateDate
) {}
