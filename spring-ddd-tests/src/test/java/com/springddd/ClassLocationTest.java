package com.springddd;

import org.junit.jupiter.api.Test;

class ClassLocationTest {
    @Test
    void printClassLocations() {
        System.out.println("IdTemp class: " + com.springddd.domain.util.IdTemp.class.getProtectionDomain().getCodeSource().getLocation());
        System.out.println("ReactiveSecurityUtils class: " + com.springddd.domain.auth.ReactiveSecurityUtils.class.getProtectionDomain().getCodeSource().getLocation());
        System.out.println("JwtAuthenticationConverter class: " + com.springddd.application.service.auth.jwt.JwtAuthenticationConverter.class.getProtectionDomain().getCodeSource().getLocation());
    }
}
