package com.bright.auth_service.record;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserRecord(
        UUID id,
        String email,
        String name,
        String password,
        LocalDateTime createDate,
        LocalDateTime updateDate
) {}
