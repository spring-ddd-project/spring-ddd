package com.springddd.application.service.gen.strategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationServiceFilePathStrategyTest {

    private final ApplicationServiceFilePathStrategy strategy = new ApplicationServiceFilePathStrategy();
    private final String projectName = "spring-ddd";
    private final Map<String, Object> context = Map.of(
            "moduleName", "user",
            "packageName", "com.springddd",
            "className", "SysUser"
    );

    @Test
    @DisplayName("supports should return true for known templates")
    void supports_knownTemplates_shouldReturnTrue() {
        assertThat(strategy.supports("command")).isTrue();
        assertThat(strategy.supports("query")).isTrue();
        assertThat(strategy.supports("view")).isTrue();
        assertThat(strategy.supports("mapstruct")).isTrue();
        assertThat(strategy.supports("pageQuery")).isTrue();
        assertThat(strategy.supports("factoryImpl")).isTrue();
        assertThat(strategy.supports("deleteDomainImpl")).isTrue();
        assertThat(strategy.supports("wipeDomainImpl")).isTrue();
        assertThat(strategy.supports("restoreDomainImpl")).isTrue();
        assertThat(strategy.supports("commandService")).isTrue();
        assertThat(strategy.supports("queryService")).isTrue();
    }

    @Test
    @DisplayName("supports should return false for unknown templates")
    void supports_unknownTemplates_shouldReturnFalse() {
        assertThat(strategy.supports("unknown")).isFalse();
        assertThat(strategy.supports("controller")).isFalse();
        assertThat(strategy.supports("entity")).isFalse();
    }

    @Test
    @DisplayName("generatePath should return correct path for command template")
    void generatePath_command_shouldReturnCorrectPath() {
        String path = strategy.generatePath("command", context, projectName);
        assertThat(path).isEqualTo("spring-ddd-application-service/src/main/java/com/springddd/application/service/user/dto/SysUserCommand.java");
    }

    @Test
    @DisplayName("generatePath should return correct path for query template")
    void generatePath_query_shouldReturnCorrectPath() {
        String path = strategy.generatePath("query", context, projectName);
        assertThat(path).isEqualTo("spring-ddd-application-service/src/main/java/com/springddd/application/service/user/dto/SysUserQuery.java");
    }

    @Test
    @DisplayName("generatePath should return correct path for view template")
    void generatePath_view_shouldReturnCorrectPath() {
        String path = strategy.generatePath("view", context, projectName);
        assertThat(path).isEqualTo("spring-ddd-application-service/src/main/java/com/springddd/application/service/user/dto/SysUserView.java");
    }

    @Test
    @DisplayName("generatePath should return correct path for mapstruct template")
    void generatePath_mapstruct_shouldReturnCorrectPath() {
        String path = strategy.generatePath("mapstruct", context, projectName);
        assertThat(path).isEqualTo("spring-ddd-application-service/src/main/java/com/springddd/application/service/user/dto/SysUserViewMapStruct.java");
    }

    @Test
    @DisplayName("generatePath should return correct path for pageQuery template")
    void generatePath_pageQuery_shouldReturnCorrectPath() {
        String path = strategy.generatePath("pageQuery", context, projectName);
        assertThat(path).isEqualTo("spring-ddd-application-service/src/main/java/com/springddd/application/service/user/dto/SysUserPageQuery.java");
    }

    @Test
    @DisplayName("generatePath should return correct path for factoryImpl template")
    void generatePath_factoryImpl_shouldReturnCorrectPath() {
        String path = strategy.generatePath("factoryImpl", context, projectName);
        assertThat(path).isEqualTo("spring-ddd-application-service/src/main/java/com/springddd/application/service/user/SysUserDomainFactoryImpl.java");
    }

    @Test
    @DisplayName("generatePath should return correct path for deleteDomainImpl template")
    void generatePath_deleteDomainImpl_shouldReturnCorrectPath() {
        String path = strategy.generatePath("deleteDomainImpl", context, projectName);
        assertThat(path).isEqualTo("spring-ddd-application-service/src/main/java/com/springddd/application/service/user/DeleteSysUserDomainServiceImpl.java");
    }

    @Test
    @DisplayName("generatePath should return correct path for wipeDomainImpl template")
    void generatePath_wipeDomainImpl_shouldReturnCorrectPath() {
        String path = strategy.generatePath("wipeDomainImpl", context, projectName);
        assertThat(path).isEqualTo("spring-ddd-application-service/src/main/java/com/springddd/application/service/user/WipeSysUserDomainServiceImpl.java");
    }

    @Test
    @DisplayName("generatePath should return correct path for restoreDomainImpl template")
    void generatePath_restoreDomainImpl_shouldReturnCorrectPath() {
        String path = strategy.generatePath("restoreDomainImpl", context, projectName);
        assertThat(path).isEqualTo("spring-ddd-application-service/src/main/java/com/springddd/application/service/user/RestoreSysUserDomainServiceImpl.java");
    }

    @Test
    @DisplayName("generatePath should return correct path for commandService template")
    void generatePath_commandService_shouldReturnCorrectPath() {
        String path = strategy.generatePath("commandService", context, projectName);
        assertThat(path).isEqualTo("spring-ddd-application-service/src/main/java/com/springddd/application/service/user/SysUserCommandService.java");
    }

    @Test
    @DisplayName("generatePath should return correct path for queryService template")
    void generatePath_queryService_shouldReturnCorrectPath() {
        String path = strategy.generatePath("queryService", context, projectName);
        assertThat(path).isEqualTo("spring-ddd-application-service/src/main/java/com/springddd/application/service/user/SysUserQueryService.java");
    }

    @Test
    @DisplayName("generatePath should return null for unsupported template")
    void generatePath_unsupported_shouldReturnNull() {
        String path = strategy.generatePath("unsupported", context, projectName);
        assertThat(path).isNull();
    }
}
