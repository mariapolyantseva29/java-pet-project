package com.bright.thought.record;

import java.time.LocalDateTime;
import java.util.UUID;

public record ThoughtRecord(
        UUID id,
        UUID situationId,
        String criticalContent,
        String realContent,
        LocalDateTime createDate,
        LocalDateTime updateDate
) {}
