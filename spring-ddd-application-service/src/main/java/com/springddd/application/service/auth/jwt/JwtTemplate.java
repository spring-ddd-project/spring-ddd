package com.springddd.application.service.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Configuration
public class JwtTemplate {

    private final String SECRET_KEY;

    private final Integer TTL;

    public JwtTemplate(JwtSecret jwtSecret) {
        this.SECRET_KEY = jwtSecret.getKey();
        this.TTL = jwtSecret.getTtl();
    }

    public String generateToken(Map<String, Object> map) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationDateTime = now.plusDays(TTL);
        return Jwts.builder()
                .claims(map)
                .issuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .expiration(Date.from(expirationDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), Jwts.SIG.HS256)
                .compact();
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseSignedClaims(token);
    }
}