package com.epiis.apirfds220261.controller;

import java.math.BigDecimal;
import java.time.Instant;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.epiis.apirfds220261.entity.EntityUser;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey key;
    private final long expirationMs;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-ms}") long expirationMs
    ) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expirationMs = expirationMs;
    }

    public String generateToken(EntityUser user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusMillis(expirationMs);

        return Jwts.builder()
            .subject(user.getIdUser())
            .claim("role", user.getRole())
            .claim("iat", toNumericDate(now))
            .claim("exp", toNumericDate(expiresAt))
            .signWith(key)
            .compact();
    }

    private BigDecimal toNumericDate(Instant instant) {
        return BigDecimal.valueOf(instant.getEpochSecond())
            .add(BigDecimal.valueOf(instant.getNano(), 9));
    }

    public SecretKey getKey() {
        return key;
    }
}