package com.bright.gateway.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@ConfigurationProperties(prefix = "app.authentication")
public record AuthenticationProperties(
    String jwtSecret
) {
    public SecretKey secretKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
