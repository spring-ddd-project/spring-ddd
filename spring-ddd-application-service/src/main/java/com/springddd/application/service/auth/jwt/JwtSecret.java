package com.springddd.application.service.auth.jwt;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class JwtSecret {
    @Value("${jwt.secret}")
    private String key;
}
