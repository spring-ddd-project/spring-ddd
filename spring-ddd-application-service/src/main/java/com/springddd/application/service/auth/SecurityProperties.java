package com.springddd.application.service.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "spring.security")
@Data
public class SecurityProperties {
    private List<String> ignorePaths = new ArrayList<>();
}
