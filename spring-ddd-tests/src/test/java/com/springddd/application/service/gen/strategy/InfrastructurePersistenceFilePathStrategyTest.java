package com.springddd.application.service.gen.strategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class InfrastructurePersistenceFilePathStrategyTest {

    private final InfrastructurePersistenceFilePathStrategy strategy = new InfrastructurePersistenceFilePathStrategy();
    private final String projectName = "spring-ddd";
    private final Map<String, Object> context = Map.of(
            "packageName", "com.springddd",
            "className", "SysUser"
    );

    @Test
    @DisplayName("supports should return true for known templates")
    void supports_knownTemplates_shouldReturnTrue() {
        assertThat(strategy.supports("entity")).isTrue();
        assertThat(strategy.supports("r2dbc")).isTrue();
        assertThat(strategy.supports("domainRepositoryImpl")).isTrue();
    }

    @Test
    @DisplayName("supports should return false for unknown templates")
    void supports_unknownTemplates_shouldReturnFalse() {
        assertThat(strategy.supports("unknown")).isFalse();
        assertThat(strategy.supports("controller")).isFalse();
        assertThat(strategy.supports("sql")).isFalse();
    }

    @Test
    @DisplayName("generatePath should return correct path for entity template")
    void generatePath_entity_shouldReturnCorrectPath() {
        String path = strategy.generatePath("entity", context, projectName);
        assertThat(path).isEqualTo("spring-ddd-infrastructure-persistence/src/main/java/com/springddd/infrastructure/persistence/entity/SysUserEntity.java");
    }

    @Test
    @DisplayName("generatePath should return correct path for r2dbc template")
    void generatePath_r2dbc_shouldReturnCorrectPath() {
        String path = strategy.generatePath("r2dbc", context, projectName);
        assertThat(path).isEqualTo("spring-ddd-infrastructure-persistence/src/main/java/com/springddd/infrastructure/persistence/r2dbc/SysUserRepository.java");
    }

    @Test
    @DisplayName("generatePath should return correct path for domainRepositoryImpl template")
    void generatePath_domainRepositoryImpl_shouldReturnCorrectPath() {
        String path = strategy.generatePath("domainRepositoryImpl", context, projectName);
        assertThat(path).isEqualTo("spring-ddd-infrastructure-persistence/src/main/java/com/springddd/infrastructure/persistence/SysUserDomainRepositoryImpl.java");
    }

    @Test
    @DisplayName("generatePath should return null for unsupported template")
    void generatePath_unsupported_shouldReturnNull() {
        String path = strategy.generatePath("unsupported", context, projectName);
        assertThat(path).isNull();
    }
}
