package com.hospital.hospital_spring.service;

import com.hospital.hospital_spring.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY =
            "hospital-spring-jwt-secret-key-change-this-later-123456";

    private static final long EXPIRATION_TIME =
            1000L * 60 * 60; // 1 hour

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateToken(User user) {

        Date issuedAt = new Date();

        Date expiration = new Date(
                issuedAt.getTime() + EXPIRATION_TIME
        );

        return Jwts.builder()

                .subject(user.takeUsername())

                .claim("userId", user.takeUserId())

                .claim("role", user.takeRole())

                .issuedAt(issuedAt)

                .expiration(expiration)

                .signWith(getSigningKey())

                .compact();
    }

    public boolean isTokenValid(String token) {

        try {

            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (Exception exception) {

            return false;
        }
    }

    public String extractUsername(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    public int extractUserId(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Number userId = claims.get("userId", Number.class);

        return userId.intValue();
    }

    public String extractRole(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("role", String.class);
    }
}