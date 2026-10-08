package com.bright.gateway.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class BucketService {
    private final String ID_TEMPLATE = ":bucket";
    private final int CAPACITY = 10;
    private final int TIME_FOR_DELETING = 30;

    private final StringRedisTemplate redisTemplate;
    private final DefaultRedisScript<Long> redisScript;

    public BucketService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.redisScript = new DefaultRedisScript<>();
        this.redisScript.setScriptSource(
                new ResourceScriptSource(new ClassPathResource("lua/bucket_update.lua"))
        );
        this.redisScript.setResultType(Long.class);
    }

    public void create(UUID userId) {
        String bucketId = hashKey(userId);
        long dateOfCreation = Instant.now().getEpochSecond();

        redisTemplate.opsForHash().put(bucketId, "available_requests", String.valueOf(CAPACITY));
        redisTemplate.opsForHash().put(bucketId, "update_date", String.valueOf(dateOfCreation));

        redisTemplate.expire(bucketId, Duration.ofMinutes(TIME_FOR_DELETING));
    }

    public boolean exist(UUID userId) {
        String bucketId = hashKey(userId);

        return redisTemplate.hasKey(bucketId);
    }

    public boolean availableRequest(UUID userId) {
        long result = redisTemplate.execute(
                redisScript,
                List.of(hashKey(userId)),
                String.valueOf(Instant.now().getEpochSecond()),
                "10",
                "2"
        );

        return result == 1L;
    }

    private String hashKey(UUID userId) {
        return userId + ID_TEMPLATE;
    }
}
