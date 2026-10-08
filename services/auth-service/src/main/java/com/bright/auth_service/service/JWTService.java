package com.bright.auth_service.service;

import com.bright.auth_service.config.AuthenticationProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JWTService {
    private final AuthenticationProperties authProperties;

    private final SecretKey SECRET_KEY;

    public JWTService(AuthenticationProperties authProperties) {
        this.authProperties = authProperties;
        this.SECRET_KEY = authProperties.secretKey();
    }

    public String create(UUID id, String email) {
        // Строим JWT
        String jwt = Jwts.builder()
                .id(id.toString())
                // setSubject() кому выдан токен (обычно id пользователя)
                .setSubject(email)

                // setIssuer("my-app"): "Кем" выдан токен (имя вашего сервиса).
                .setIssuer("thought-audit")

                // setIssuedAt(new Date()): Время выпуска токена.
                .setIssuedAt(new Date())

                // setExpiration(expirationDate): Время, когда токен станет невалидным.
                .setExpiration(expirationDate())

                .claim("id", id)
                // signWith(key): Подписываем токен нашим секретным ключом.
                // Алгоритм выбирается автоматически исходя из типа ключа.
                .signWith(SECRET_KEY)

                // compact(): Превращает всё это в одну длинную строку (сам JWT).
                .compact();

        System.out.println("\nСгенерированный JWT:");
        System.out.println(jwt);

        return jwt;
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public UUID extractId(String token) {
        return UUID.fromString(extractClaim(token, Claims::getId));
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Date expirationDate() {
        long expirationTime = System.currentTimeMillis() + 3_600_000;
        return new Date(expirationTime);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
