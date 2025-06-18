package com.springddd.application.service.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.Map;

@Configuration
public class JwtTemplate {

    private final String SECRET_KEY = "r)],&w1%g$26idpK7@.UmZ-=SXwy_Hkb-hf~3ebr,2QHI8YEIbg3_&tMW8UwzQ1f";

    public String generateToken(Map<String, Object> map) {
        return Jwts.builder()
                .claims(map)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
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