package com.springddd;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpringDDDLauncherApplicationTest {

    @Test
    void shouldHaveMainMethod() throws NoSuchMethodException {
        var mainMethod = SpringDDDLauncherApplication.class.getMethod("main", String[].class);
        assertNotNull(mainMethod);
        assertEquals(void.class, mainMethod.getReturnType());
    }

    @Test
    void shouldHaveSpringDDDLauncherApplicationClass() {
        assertNotNull(SpringDDDLauncherApplication.class);
    }

    @Test
    void shouldHaveApplicationAnnotation() {
        var springBootApplication = SpringDDDLauncherApplication.class.getAnnotation(
                org.springframework.boot.autoconfigure.SpringBootApplication.class);
        assertNotNull(springBootApplication);
    }

    @Test
    void shouldHaveComponentScanAnnotation() {
        var componentScan = SpringDDDLauncherApplication.class.getAnnotation(
                org.springframework.context.annotation.ComponentScan.class);
        assertNotNull(componentScan);
    }

    @Test
    void shouldHaveEnableConfigurationPropertiesAnnotation() {
        var enableConfigProps = SpringDDDLauncherApplication.class.getAnnotation(
                org.springframework.boot.context.properties.EnableConfigurationProperties.class);
        assertNotNull(enableConfigProps);
    }

    @Test
    void shouldHaveReactiveUserDetailsServiceAnnotation() {
        var enableReactiveUserDetailsService = SpringDDDLauncherApplication.class.getAnnotation(
                org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity.class);
        assertNotNull(enableReactiveUserDetailsService);
    }
}
