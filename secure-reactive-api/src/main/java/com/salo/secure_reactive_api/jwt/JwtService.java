package com.salo.secure_reactive_api.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class JwtService {

    private final Key key;
    private final long expMinutes;

    public JwtService(String secret, long expMinutes) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expMinutes = expMinutes;
    }

    public String generate(String subjectEmail, List<String> roles) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expMinutes * 60);

        return Jwts.builder()
                .setSubject(subjectEmail)
                .claim("roles", roles)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
}