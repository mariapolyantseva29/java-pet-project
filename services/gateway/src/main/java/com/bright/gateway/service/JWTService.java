package com.bright.gateway.service;

import com.bright.gateway.config.AuthenticationProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JWTService {
    private final SecretKey SECRET_KEY;

    public JWTService(AuthenticationProperties authProperties) {
        this.SECRET_KEY = authProperties.secretKey();
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public UUID extractId(String token) {
        return UUID.fromString(extractClaim(token, Claims::getId));
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch(SignatureException e) {
            System.err.println("Невалидная подпись JWT: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.err.println("Срок действия токена истек: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.err.println("Некорректный формат токена: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("Неподдерживаемый JWT токен: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Пустой токен: " + e.getMessage());
        }

        return false;
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
